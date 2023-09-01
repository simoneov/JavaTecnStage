package com.simone.FactoryDemo.seeding;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.simone.FactoryDemo.enums.DurationType;
import com.simone.FactoryDemo.enums.MachineState;
import com.simone.FactoryDemo.enums.OperationState;
import com.simone.FactoryDemo.enums.OrderState;
import com.simone.FactoryDemo.models.Item;
import com.simone.FactoryDemo.models.CycleOperation;
import com.simone.FactoryDemo.models.Machine;
import com.simone.FactoryDemo.models.Order;
import com.simone.FactoryDemo.models.ProductionCycle;
import com.simone.FactoryDemo.models.Operation;
import com.simone.FactoryDemo.repositories.CycleOperationRepository;
import com.simone.FactoryDemo.repositories.ItemRepository;
import com.simone.FactoryDemo.repositories.MachineRepository;
import com.simone.FactoryDemo.repositories.OperationRepository;
import com.simone.FactoryDemo.repositories.OrderRepository;
import com.simone.FactoryDemo.repositories.ProductionCycleRepository;

@Component
public class DatabaseSeeder {

	@Autowired
	private MachineRepository machineRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OperationRepository operationRepository;
	@Autowired
	private ProductionCycleRepository productionCycleRepository;
	@Autowired
	private CycleOperationRepository cycleOperationRepository;
	
	
	@EventListener
	public void seed(ContextRefreshedEvent event) 
	{
//		operationRepository.deleteAll();
//		orderRepository.deleteAll();
//		machineRepository.deleteAll();
//		itemRepository.deleteAll();
		
		seedProductionCycleTable();
	    seedMachineTable();
	    seedItemTable();
	    try {
			seedOrderTable();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    seedOperationTable();
	    seedCycleOperationTable();
    }

	
	private void seedCycleOperationTable() {
		if(cycleOperationRepository.findAll().size()==0)
		{
			List<CycleOperation> cycleOperations = Arrays.asList(
					CycleOperation
					.builder()
					.productionCycleId(productionCycleRepository.findByCode("FB00").getId())
					.operationCode(10)
					.description("impasto")
					.machineId(machineRepository.findByCode("IMP002").getId())
					.type(DurationType.INV)
					.invariantDuration(15)
					.build(),
					CycleOperation
					.builder()
					.productionCycleId(productionCycleRepository.findByCode("FB00").getId())
					.operationCode(20)
					.description("infornata")
					.machineId(machineRepository.findByCode("FRN001").getId())
					.type(DurationType.INV)
					.invariantDuration(45)
					.build(),
					CycleOperation
					.builder()
					.productionCycleId(productionCycleRepository.findByCode("FB00").getId())
					.operationCode(30)
					.description("confezionamento")
					.machineId(machineRepository.findByCode("CONF02").getId())
					.type(DurationType.VAR)
					.workingTime(12)
					.workingQuantity(10)
					.build(),
					CycleOperation
					.builder()
					.productionCycleId(productionCycleRepository.findByCode("BC00").getId())
					.operationCode(10)
					.description("impasto")
					.machineId(machineRepository.findByCode("IMP001").getId())
					.type(DurationType.INV)
					.invariantDuration(20)
					.build(),
					CycleOperation
					.builder()
					.productionCycleId(productionCycleRepository.findByCode("BC00").getId())
					.operationCode(20)
					.description("infornata")
					.machineId(machineRepository.findByCode("FRN001").getId())
					.type(DurationType.INV)
					.invariantDuration(30)
					.build(),
					CycleOperation
					.builder()
					.productionCycleId(productionCycleRepository.findByCode("BC00").getId())
					.operationCode(30)
					.description("confezionamento")
					.machineId(machineRepository.findByCode("CONF01").getId())
					.type(DurationType.VAR)
					.workingTime(15)
					.workingQuantity(10)
					.build(),
					CycleOperation
					.builder()
					.productionCycleId(productionCycleRepository.findByCode("BS00").getId())
					.operationCode(10)
					.description("impasto")
					.machineId(machineRepository.findByCode("IMP001").getId())
					.type(DurationType.INV)
					.invariantDuration(20)
					.build(),
					CycleOperation
					.builder()
					.productionCycleId(productionCycleRepository.findByCode("BS00").getId())
					.operationCode(20)
					.description("infornata")
					.machineId(machineRepository.findByCode("FRN001").getId())
					.type(DurationType.INV)
					.invariantDuration(30)
					.build(),
					CycleOperation
					.builder()
					.productionCycleId(productionCycleRepository.findByCode("BS00").getId())
					.operationCode(30)
					.description("confezionamento")
					.machineId(machineRepository.findByCode("CONF01").getId())
					.type(DurationType.VAR)
					.workingTime(20)
					.workingQuantity(5)
					.build()
					);
			cycleOperationRepository.saveAll(cycleOperations);
			
		}
	}

	
	private void seedProductionCycleTable() {
		if(productionCycleRepository.findAll().size()==0)
		{
			List<ProductionCycle> productionCycles = Arrays.asList(
					ProductionCycle
					.builder()
					.code("FB00")
					.description("fette biscottate")
					.build(),
					ProductionCycle
					.builder()
					.code("BC00")
					.description("biscotti budget")
					.build(),
					ProductionCycle
					.builder()
					.code("BS00")
					.description("biscotti premium")
					.build()
					); 
			productionCycleRepository.saveAll(productionCycles);
		}
	}
	

	private void seedOperationTable() {
		if(operationRepository.findAll().size()==0) 
		{
			
			List<Operation> operations = Arrays.asList(
					Operation
					.builder()
					.code(10)
					.description("impasto")
					.machineId(machineRepository.findByCode("IMP001").getId())
					.scrappedQuantity(0)
					.durataMin(20)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10122L).getId())
					.build(),
					Operation
					.builder()
					.code(20)
					.description("infornata")
					.machineId(machineRepository.findByCode("FRN001").getId())
					.scrappedQuantity(0)
					.durataMin(30)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10122L).getId())
					.build(),
					Operation
					.builder()
					.code(10)
					.description("impasto")
					.machineId(machineRepository.findByCode("IMP001").getId())
					.scrappedQuantity(0)
					.durataMin(20)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10123L).getId())
					.build(),
					Operation
					.builder()
					.code(20)
					.description("infornata")
					.machineId(machineRepository.findByCode("FRN001").getId())
					.scrappedQuantity(0)
					.durataMin(30)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10123L).getId())
					.build(),
					Operation
					.builder()
					.code(10)
					.description("impasto")
					.machineId(machineRepository.findByCode("IMP002").getId())
					.scrappedQuantity(0)
					.durataMin(15)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10124L).getId())
					.build(),
					Operation
					.builder()
					.code(20)
					.description("infornata")
					.machineId(machineRepository.findByCode("FRN001").getId())
					.scrappedQuantity(0)
					.durataMin(45)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10124L).getId())
					.build(),
					Operation
					.builder()
					.code(10)
					.description("impasto")
					.machineId(machineRepository.findByCode("IMP001").getId())
					.scrappedQuantity(0)
					.durataMin(20)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10125L).getId())
					.build(),
					Operation
					.builder()
					.code(20)
					.description("infornata")
					.machineId(machineRepository.findByCode("FRN001").getId())
					.scrappedQuantity(0)
					.durataMin(30)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10125L).getId())
					.build(),
					Operation
					.builder()
					.code(10)
					.description("impasto")
					.machineId(machineRepository.findByCode("IMP002").getId())
					.scrappedQuantity(0)
					.durataMin(15)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10126L).getId())
					.build(),
					Operation
					.builder()
					.code(20)
					.description("infornata")
					.machineId(machineRepository.findByCode("FRN001").getId())
					.scrappedQuantity(0)
					.durataMin(45)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10126L).getId())
					.build(),
					Operation
					.builder()
					.code(10)
					.description("impasto")
					.machineId(machineRepository.findByCode("IMP001").getId())
					.scrappedQuantity(0)
					.durataMin(20)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10127L).getId())
					.build(),
					Operation
					.builder()
					.code(20)
					.description("infornata")
					.machineId(machineRepository.findByCode("FRN001").getId())
					.scrappedQuantity(0)
					.durataMin(30)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10127L).getId())
					.build(),
					Operation
					.builder()
					.code(10)
					.description("impasto")
					.machineId(machineRepository.findByCode("IMP002").getId())
					.scrappedQuantity(0)
					.durataMin(15)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10128L).getId())
					.build(),
					Operation
					.builder()
					.code(20)
					.description("infornata")
					.machineId(machineRepository.findByCode("FRN001").getId())
					.scrappedQuantity(0)
					.durataMin(45)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10128L).getId())
					.build(),
					Operation
					.builder()
					.code(10)
					.description("impasto")
					.machineId(machineRepository.findByCode("IMP002").getId())
					.scrappedQuantity(0)
					.durataMin(15)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10129L).getId())
					.build(),
					Operation
					.builder()
					.code(20)
					.description("infornata")
					.machineId(machineRepository.findByCode("FRN001").getId())
					.scrappedQuantity(0)
					.durataMin(45)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10129L).getId())
					.build(),
					Operation
					.builder()
					.code(10)
					.description("impasto")
					.machineId(machineRepository.findByCode("IMP002").getId())
					.scrappedQuantity(0)
					.durataMin(15)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10130L).getId())
					.build(),
					Operation
					.builder()
					.code(20)
					.description("infornata")
					.machineId(machineRepository.findByCode("FRN001").getId())
					.scrappedQuantity(0)
					.durataMin(45)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10130L).getId())
					.build(),
					Operation
					.builder()
					.code(10)
					.description("impasto")
					.machineId(machineRepository.findByCode("IMP001").getId())
					.scrappedQuantity(0)
					.durataMin(20)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10131L).getId())
					.build(),
					Operation
					.builder()
					.code(20)
					.description("infornata")
					.machineId(machineRepository.findByCode("FRN001").getId())
					.scrappedQuantity(0)
					.durataMin(30)
					.producedQuantity(0)
					.state(OperationState.NEW)
					.orderId(orderRepository.findByCode(10131L).getId())
					.build());
			operationRepository.saveAll(operations);
		}				
	}



	private void seedOrderTable() throws ParseException {
		if(orderRepository.findAll().size()==0) 
		{
			//new SimpleDateFormat("yyyy-MM-dd").parse("2023-06-15")
			
			
			List<Order> orders = Arrays.asList(
					Order
					.builder()
					.itemId(itemRepository.findByCode("BCSBR01").getId())
					.code(10122L)
					.deliveryDate(ZonedDateTime.of(LocalDateTime.of(2023,6,15,0,0),ZoneId.of("UTC")))
					.requestedQuantity(50)
					.producedQuantity(0)
					.scrappedQuantity(0)
					.state(OrderState.NEW)
					.build(),
					Order
					.builder()
					.deliveryDate(ZonedDateTime.of(LocalDateTime.of(2023,6,15,0,0),ZoneId.of("UTC")))
					.requestedQuantity(120)
					.producedQuantity(0)
					.scrappedQuantity(0)
					.itemId(itemRepository.findByCode("BSCIN02").getId())
					.code(10123L)
					.state(OrderState.NEW)
					.build(),
					Order
					.builder()
					.deliveryDate(ZonedDateTime.of(LocalDateTime.of(2023,6,16,0,0),ZoneId.of("UTC")))					
					.requestedQuantity(200)
					.producedQuantity(0)
					.scrappedQuantity(0)
					.itemId(itemRepository.findByCode("FBSDC01").getId())
					.code(10124L)
					.state(OrderState.NEW)
					.build(),
					Order
					.builder()
					.deliveryDate(ZonedDateTime.of(LocalDateTime.of(2023,6,17,0,0),ZoneId.of("UTC")))
					.requestedQuantity(110)
					.producedQuantity(0)
					.scrappedQuantity(0)
					.itemId(itemRepository.findByCode("BCSBR01").getId())
					.code(10125L)
					.state(OrderState.NEW)
					.build(),
					Order
					.builder()
					.deliveryDate(ZonedDateTime.of(LocalDateTime.of(2023,6,18,0,0),ZoneId.of("UTC")))
					.requestedQuantity(100)
					.producedQuantity(0)
					.scrappedQuantity(0)
					.itemId(itemRepository.findByCode("FBSIN02").getId())
					.code(10126L)
					.state(OrderState.NEW)
					.build(),
					Order
					.builder()
					.deliveryDate(ZonedDateTime.of(LocalDateTime.of(2023,6,19,0,0),ZoneId.of("UTC")))
					.requestedQuantity(50)
					.producedQuantity(0)
					.scrappedQuantity(0)
					.itemId(itemRepository.findByCode("BSCCC03").getId())
					.code(10127L)
					.state(OrderState.NEW)
					.build(),
					Order
					.builder()
					.deliveryDate(ZonedDateTime.of(LocalDateTime.of(2023,6,20,0,0),ZoneId.of("UTC")))
					.requestedQuantity(110)
					.producedQuantity(0)
					.scrappedQuantity(0)
					.itemId(itemRepository.findByCode("FBSIN02").getId())
					.code(10128L)
					.state(OrderState.NEW)
					.build(),
					Order
					.builder()
					.deliveryDate(ZonedDateTime.of(LocalDateTime.of(2023,6,18,0,0),ZoneId.of("UTC")))
					.requestedQuantity(80)
					.producedQuantity(0)
					.scrappedQuantity(0)
					.itemId(itemRepository.findByCode("FBSMT03").getId())
					.code(10129L)
					.state(OrderState.NEW)
					.build(),
					Order
					.builder()
					.deliveryDate(ZonedDateTime.of(LocalDateTime.of(2023,6,19,0,0),ZoneId.of("UTC")))
					.requestedQuantity(120)
					.producedQuantity(0)
					.scrappedQuantity(0)
					.itemId(itemRepository.findByCode("FBSIN02").getId())
					.code(10130L)
					.state(OrderState.NEW)
					.build(),
					Order
					.builder()
					.deliveryDate(ZonedDateTime.of(LocalDateTime.of(2023,6,20,0,0),ZoneId.of("UTC")))
					.requestedQuantity(150)
					.producedQuantity(0)
					.scrappedQuantity(0)
					.itemId(itemRepository.findByCode("BSCIN02").getId())
					.code(10131L)
					.state(OrderState.NEW)
					.build());
			orderRepository.saveAll(orders);
		}			
	}



	private void seedItemTable() {
		if(itemRepository.findAll().size()==0) 
		{
			
			List<Item> items = Arrays.asList(
					Item
					.builder()
					.code("FBSDC01")
					.quantity(100)
					.description("fette biscottate dolcificate")
					.productionCycleId(productionCycleRepository.findByCode("FB00").getId())
					.build(),
					Item
					.builder()
					.code("FBSIN02")
					.quantity(100)
					.description("fette biscottate integrali")
					.productionCycleId(productionCycleRepository.findByCode("FB00").getId())
					.build(),
					Item
					.builder()
					.code("FBSMT03")
					.quantity(200)
					.description("fette biscottate al malto")
					.productionCycleId(productionCycleRepository.findByCode("FB00").getId())
					.build(),
					Item
					.builder()
					.code("BCSBR01")
					.quantity(150)
					.description("biscotti al burro")
					.productionCycleId(productionCycleRepository.findByCode("BC00").getId())
					.build(),
					Item
					.builder()
					.code("BSCIN02")
					.quantity(250)
					.description("biscotti integrali")
					.productionCycleId(productionCycleRepository.findByCode("BS00").getId())
					.build(),
					Item
					.builder()
					.code("BSCCC03")
					.quantity(50)
					.description("biscotti al cacao")
					.productionCycleId(productionCycleRepository.findByCode("BS00").getId())
					.build());
			itemRepository.saveAll(items);
		}		
	}

	private void seedMachineTable() {
		if(machineRepository.findAll().size()==0) 
		{
			
			List<Machine> machines = Arrays.asList(
					Machine
					.builder()
					.code("IMP001")
					.state(MachineState.STOP)
					.description("impastatrice 1")
					.build(),
					Machine
					.builder()
					.code("IMP002")
					.state(MachineState.STOP)
					.description("impastatrice 2")
					.build(),
					Machine
					.builder()
					.code("FRN001")
					.state(MachineState.STOP)
					.description("forno 1")
					.build(),
					Machine
					.builder()
					.code("CONF01")
					.state(MachineState.STOP)
					.description("linea confezionamento 1")
					.build(),
					Machine
					.builder()
					.code("CONF02")
					.state(MachineState.STOP)
					.description("linea confezionamento 2")
					.build());
			machineRepository.saveAll(machines);
		}
	}

}
