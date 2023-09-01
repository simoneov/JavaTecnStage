package com.simone.FactoryDemo.services.implementations;

import java.util.List;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.simone.FactoryDemo.dtos.insertdtos.GenerateOrderWithOperationsDto;
import com.simone.FactoryDemo.dtos.insertdtos.InsertOrderDto;
import com.simone.FactoryDemo.enums.DurationType;
import com.simone.FactoryDemo.enums.OperationState;
import com.simone.FactoryDemo.enums.OrderState;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.CycleOperation;
import com.simone.FactoryDemo.models.Item;
import com.simone.FactoryDemo.models.Operation;
import com.simone.FactoryDemo.models.Order;
import com.simone.FactoryDemo.repositories.CycleOperationRepository;
import com.simone.FactoryDemo.repositories.ItemRepository;
import com.simone.FactoryDemo.repositories.OperationRepository;
import com.simone.FactoryDemo.repositories.OrderRepository;
import com.simone.FactoryDemo.services.interfaces.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	OrderRepository orderRepository;
	OperationRepository operationRepository;
	ItemRepository itemRepository;
	CycleOperationRepository cycleOperationRepository;

	public OrderServiceImpl(OrderRepository orderRepository, ItemRepository itemRepository,
			OperationRepository operationRepository, CycleOperationRepository cycleOperationRepository) {
		this.orderRepository = orderRepository;
		this.operationRepository = operationRepository;
		this.itemRepository = itemRepository;
		this.cycleOperationRepository = cycleOperationRepository;
	}

	@Override
	public Order findByCode(Long code) {
		Order order = orderRepository.findByCode(code);
		return order;
	}

	@Override
	public List<Order> getAll() {
		return orderRepository.findAll();
	}

	@Override
	public void update(Order order) 
	{
		Order orderToUpdate = orderRepository.findByCode(order.getCode());

		if(orderToUpdate != null) 
		{
			orderToUpdate.setProducedQuantity(order.getProducedQuantity());
			orderToUpdate.setScrappedQuantity(order.getScrappedQuantity());
			orderToUpdate.setStartDate(order.getStartDate());
			orderToUpdate.setEndDate(order.getEndDate());
			orderToUpdate.setState(order.getState());
			orderRepository.save(orderToUpdate);
		}
	}

	@Override
	public void create(InsertOrderDto orderToInsert) throws InvalidOperationException {
		
		
		if(orderToInsert.getItemCode() == null || itemRepository.findByCode(orderToInsert.getItemCode()) == null) 
			throw new InvalidOperationException("You have to insert a valid item code",HttpStatus.BAD_REQUEST);
		ZonedDateTime today = ZonedDateTime.now();
		if(orderToInsert.getDeliveryDate() == null || orderToInsert.getDeliveryDate().isBefore(today))
			throw new InvalidOperationException("The delivery day must not be null and must be after the current date",HttpStatus.BAD_REQUEST);
		orderToInsert.setItemCode(itemRepository.findByCode(orderToInsert.getItemCode()).getId().toString());
		Order order = orderToInsert.toModel();
		order.setState(OrderState.NEW);
		orderRepository.save(order);	
		
	}

	@Override
	public void delete(Long code) {
		orderRepository.removeByCode(code);
	}

	@Override
	public void endOrder(Long code) throws InvalidOperationException {
		Order order = orderRepository.findByCode(code);
		if(order == null)
			throw new InvalidOperationException("This order doesn't exist", HttpStatus.NOT_FOUND);
		List<Operation> orderOperations = operationRepository.operationsPerOrder(order.getId());
		if(orderOperations.size() == 0)
			throw new InvalidOperationException("No operation runned on this order, therefor it doesn't make sense to stop it", HttpStatus.BAD_REQUEST);
		if(!orderOperations.stream().allMatch(o->o.getState()== OperationState.COMPLETED) || order.getState() != OrderState.INPRODUCTION)
			throw new InvalidOperationException("You can't end an order not in production or with incomplete operations", HttpStatus.BAD_REQUEST);
		order.setState(OrderState.COMPLETED);
		order.setEndDate(ZonedDateTime.now(ZoneId.of("UTC")));
		Item itemToUpdate = itemRepository.findById(order.getItemId())
				.orElseThrow(()->new InvalidOperationException("Non-existent item", HttpStatus.BAD_REQUEST));
		itemToUpdate.setQuantity(itemToUpdate.getQuantity() + order.getProducedQuantity());
		orderRepository.save(order);
	}

	@Override
	public void generateOrderWithOperations(GenerateOrderWithOperationsDto generateOrderDto) throws InvalidOperationException {
		if(generateOrderDto.getItemCode() == null)
			throw new InvalidOperationException("You can't generate an order without the code of the item",HttpStatus.BAD_REQUEST);
		Item itemToOrder = itemRepository.findByCode(generateOrderDto.getItemCode());
		if(generateOrderDto.getQuantity() == null || generateOrderDto.getQuantity() < 0)
			throw new InvalidOperationException("To do an order you have to insert "
					+ "even the requested quantity of the item",HttpStatus.BAD_REQUEST);
		//LocalDateTime ldt = LocalDateTime.ofInstant(generateOrderDto.getDeliveryDate().toInstant(), ZoneId.systemDefault());
		if(generateOrderDto.getDeliveryDate() == null || ZonedDateTime.ofInstant(generateOrderDto.getDeliveryDate().toInstant(), ZoneId.of("UTC")).isBefore(ZonedDateTime.now()))
			throw new InvalidOperationException("Please insert a valid date",HttpStatus.BAD_REQUEST);
		Order orderToCreate = Order.builder().code(orderRepository.lastOrder() != null ? orderRepository.lastOrder()+1 : 10000L)
				.deliveryDate(Instant.ofEpochMilli(generateOrderDto.getDeliveryDate().getTime())
					      .atZone(ZoneId.of("UTC"))

						)
				.producedQuantity(0)
				.state(OrderState.NEW)
				.scrappedQuantity(0)
				.requestedQuantity(generateOrderDto.getQuantity())
				.itemId(itemToOrder.getId()).build();
		orderRepository.save(orderToCreate);
		List<CycleOperation> cycleOperations = cycleOperationRepository.findAllCycleOperationsByCycle(itemToOrder.getProductionCycleId());
		List<Operation> orderOperations = new ArrayList<>();
		cycleOperations
		.stream()
		.forEach(o -> { 
			Operation op = Operation.builder()
					.code(o.getOperationCode())
					.description(o.getDescription())
					.producedQuantity(0)
					.scrappedQuantity(0)
					.state(OperationState.NEW)					
					.orderId(orderToCreate.getId())
					.machineId(o.getMachineId())
					.build();
			if(o.getType()==DurationType.VAR && (o.getWorkingTime() * generateOrderDto.getQuantity() == 0) && (o.getWorkingQuantity() == 0))
			{
				op.setDurataMin(0);
			}
			else 
			{
				op.setDurataMin(o.getType() == DurationType.INV ? 
						o.getInvariantDuration() : o.getWorkingTime() * generateOrderDto.getQuantity()/o.getWorkingQuantity());
			}
			
			orderOperations.add(op); 
			});
		
		operationRepository.saveAll(orderOperations);
	}

}
