package com.simone.FactoryDemo.services.implementations;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.simone.FactoryDemo.dtos.insertdtos.InsertOperationDto;
import com.simone.FactoryDemo.dtos.updatedtos.StartWorkDto;
import com.simone.FactoryDemo.dtos.updatedtos.SuspendWorkDto;
import com.simone.FactoryDemo.dtos.updatedtos.ProduceQuantityDto;
import com.simone.FactoryDemo.enums.MachineState;
import com.simone.FactoryDemo.enums.OperationState;
import com.simone.FactoryDemo.enums.OrderState;

import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.Machine;
import com.simone.FactoryDemo.models.Operation;
import com.simone.FactoryDemo.models.Order;
import com.simone.FactoryDemo.repositories.MachineRepository;
import com.simone.FactoryDemo.repositories.OperationRepository;
import com.simone.FactoryDemo.repositories.OrderRepository;
import com.simone.FactoryDemo.services.interfaces.OperationService;
import com.simone.FactoryDemo.viewmodels.OperationViewModel;

@Service
public class OperationServiceImpl implements OperationService {

	OperationRepository operationRepository;
	OrderRepository orderRepository;
	MachineRepository machineRepository;

	public OperationServiceImpl(OperationRepository operationRepository, OrderRepository orderRepository,
			MachineRepository machineRepository) 
	{
		this.machineRepository = machineRepository;
		this.operationRepository = operationRepository;
		this.orderRepository = orderRepository;
	}

	@Override
	public Operation findByCodeAndOrderCode(Integer code, Long orderCode) 
	{
		return operationRepository.findByCodeAndOrderCode(code, orderCode);
	}

	@Override
	public List<OperationViewModel> getAll() 
	{
		
		return operationRepository.findAll().stream().map( a -> OperationViewModel
				.builder()
				.code(a.getCode())
				.description(a.getDescription())
				.durataMin(a.getDurataMin())
				.endDate(a.getEndDate())
				.startDate(a.getStartDate())
				.state(a.getState().toString())
				.endDate(a.getEndDate())
				.scrappedQuantity(a.getScrappedQuantity())
				.producedQuantity(a.getProducedQuantity())
				.machineCode(machineRepository.findById(a.getMachineId()).get().getCode())
				.orderCode(orderRepository.findById(a.getOrderId()).get().getCode())
				.build())
				.toList();
	}

	@Override
	public void startWork(StartWorkDto operation) throws InvalidOperationException 
	{

		Operation operationToUpdate = operationRepository.findByCodeAndOrderCode(operation.getCode(),
				operation.getOrderCode());
		if (operationToUpdate == null) 
			throw new InvalidOperationException("Operation not found", HttpStatus.NOT_FOUND);
		Machine machineToStart = machineRepository.findByCode(operation.getMachineCode());
		if (machineToStart == null) 
			throw new InvalidOperationException(
					"The machine connected to the operation does not exist or maybe you have to insert a machine code",
					HttpStatus.NOT_FOUND);
		if (!machineToStart.getCode().equals(machineRepository.findById(operationToUpdate.getMachineId()).get().getCode())) 
			throw new InvalidOperationException("The machine  with this code exists, but is not connected to the operation that you want to start",
					HttpStatus.BAD_REQUEST);
		Order orderToUpdate = orderRepository.findByCode(operation.getOrderCode());

		if (machineToStart.getState().equals(MachineState.RUN)
				|| orderToUpdate.getState().equals(OrderState.COMPLETED)
				|| (operationToUpdate.getState().equals(OperationState.INPROGRESS)
						|| operationToUpdate.getState().equals(OperationState.COMPLETED))) 
			throw new InvalidOperationException(
					"Operation can not start because the order,"
							+ " the machine or the operation are not in the right state",
					HttpStatus.BAD_REQUEST);
		machineToStart.setState(MachineState.RUN);
		orderToUpdate.setState(OrderState.INPRODUCTION);
		operationToUpdate.setState(OperationState.INPROGRESS);
		if (operationToUpdate.getStartDate() == null) 
		{
			operationToUpdate.setStartDate(new Date());
		}
		if (orderToUpdate.getStartDate() == null) 
		{
			orderToUpdate.setStartDate(ZonedDateTime.now(ZoneId.of("UTC")));
		}
		operationRepository.save(operationToUpdate);
	}


	@Override
	public void create(InsertOperationDto operation) throws InvalidOperationException 
	{
		if (operation.getOrderCode() == null && operation.getMachineCode() == null) 
			throw new InvalidOperationException("You have to insert both an order code and a machine code", HttpStatus.BAD_REQUEST);
		Machine optMachine = machineRepository.findByCode(operation.getMachineCode());
		Order optOrder = orderRepository.findByCode(operation.getOrderCode());
		if (optMachine == null || optOrder == null) 
			throw new InvalidOperationException("The code of the machine or the code of the order or both don't correspond to an existing"
					+ "order or machine",HttpStatus.BAD_REQUEST);

		operation.setMachineCode(optMachine.getId().toString());
		Operation operationToSave = operation.toModel();
		operationToSave.setState(OperationState.NEW);
		operationToSave.setScrappedQuantity(0);
		operationToSave.setProducedQuantity(0);
		operationToSave.setOrderId(optOrder.getId());
		operationRepository.save(operationToSave);
	}

	@Override
	public void delete(Integer code, Long orderCode) {
		operationRepository.removeByCodeAndOrderCode(code, orderCode);
	}


	@Override
	public void suspendWork(SuspendWorkDto operation) throws InvalidOperationException 
	{

		Operation operationToUpdate = operationRepository.findByCodeAndOrderCode(operation.getCode(),operation.getOrderCode());
		if (operationToUpdate == null) 
			throw new InvalidOperationException("Operation not found", HttpStatus.NOT_FOUND);
		if (!operationToUpdate.getState().equals(OperationState.INPROGRESS)) 
			throw new InvalidOperationException("You can't suspend an operation that is not running",HttpStatus.BAD_REQUEST);
		Machine machineToUpdate = machineRepository.findById(operationToUpdate.getMachineId())
				.orElseThrow(() -> new InvalidOperationException("Machine not found", HttpStatus.NOT_FOUND));
		machineToUpdate.setState(MachineState.STOP);
		operationToUpdate.setState(OperationState.SUSPENDED);
		operationRepository.save(operationToUpdate);
	}


	public void produceQuantity(ProduceQuantityDto operation) throws InvalidOperationException 
	{
		Operation operationToUpdate = operationRepository.findByCodeAndOrderCode(operation.getCode(), operation.getOrderCode());
		if (operationToUpdate == null) 
			throw new InvalidOperationException("Operation not found", HttpStatus.NOT_FOUND);
		Order operationOrder = orderRepository.findByCode(operation.getOrderCode());
		if (operationToUpdate.getState() != OperationState.INPROGRESS) 
			throw new InvalidOperationException("When the operation is not in progress you can't update the pieces producted and discarded",
					HttpStatus.BAD_REQUEST);		
		if (operation.getManufacturedQuantity() < 0 || operation.getDiscardedQuantity() < 0 
				|| (operation.getManufacturedQuantity() == 0 && operation.getDiscardedQuantity() == 0)) 
			throw new InvalidOperationException("Please, insert consistent data. At least one quantity must be greater than 0 "
					+ "and both numbers can't be negative", HttpStatus.BAD_REQUEST);
		
		operationToUpdate.setProducedQuantity(operationToUpdate.getProducedQuantity() + operation.getManufacturedQuantity());
		operationToUpdate.setScrappedQuantity(operationToUpdate.getScrappedQuantity() + operation.getDiscardedQuantity());

		if (operationToUpdate.getProducedQuantity() > operationOrder.getRequestedQuantity()) 
			throw new InvalidOperationException("The quantity can't surpass the requested quantity in the order", HttpStatus.BAD_REQUEST);
		if (operationToUpdate.getCode() == operationRepository.lastOrderOperation(operationOrder.getId())) 
		{
			operationOrder.setProducedQuantity(operationToUpdate.getProducedQuantity());
			operationOrder.setScrappedQuantity(operationToUpdate.getScrappedQuantity());
		}
		operationRepository.save(operationToUpdate);
	}

	@Override
	public void endOperation(Integer operationCode, Long orderCode) throws InvalidOperationException {
		
		Operation operationToUpdate = operationRepository.findByCodeAndOrderCode(operationCode, orderCode);
		if(operationToUpdate == null)
			throw new InvalidOperationException("Operation not found", HttpStatus.NOT_FOUND);
		if(operationToUpdate.getState() == OperationState.COMPLETED || operationToUpdate.getState() == OperationState.NEW)
			throw new InvalidOperationException("You can't end a new or already completed operation", HttpStatus.BAD_REQUEST);
		Order operationOrder = orderRepository.findByCode(orderCode);
		if(operationToUpdate.getProducedQuantity() < operationOrder.getRequestedQuantity())
			throw new InvalidOperationException("You can't end an operation without reaching the requested quantity of the order", HttpStatus.BAD_REQUEST);
		operationToUpdate.setEndDate(new Date());
		machineRepository.findById(operationToUpdate.getMachineId()).get().setState(MachineState.STOP);
		operationToUpdate.setState(OperationState.COMPLETED);
		operationRepository.save(operationToUpdate);
		
	}
	


}
