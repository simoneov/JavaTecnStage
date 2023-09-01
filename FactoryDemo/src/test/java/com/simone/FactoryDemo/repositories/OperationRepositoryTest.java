package com.simone.FactoryDemo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.simone.FactoryDemo.enums.MachineState;
import com.simone.FactoryDemo.enums.OperationState;
import com.simone.FactoryDemo.enums.OrderState;
import com.simone.FactoryDemo.models.Item;
import com.simone.FactoryDemo.models.Machine;
import com.simone.FactoryDemo.models.Operation;
import com.simone.FactoryDemo.models.Order;

@DataJpaTest
public class OperationRepositoryTest {

	@Autowired
	OperationRepository operationRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	MachineRepository machineRepository;
	@Autowired
	ItemRepository itemRepository;

	
    @BeforeEach
    void init() 
    {
 
	   	machineRepository.deleteAll();
    	itemRepository.deleteAll();
    	Item item = Item.builder().code("1").description("s").quantity(4).build();
    	Machine machine = Machine.builder().code("m").description("macchina").state(MachineState.STOP).build();
    	
    	itemRepository.save(item);
    	machineRepository.save(machine);
    	Order order = Order.builder().code(1L).requestedQuantity(0).scrappedQuantity(0).producedQuantity(0).itemId(itemRepository.findByCode("1").getId()).state(OrderState.NEW).build();

    	orderRepository.save(order);
    	List<Operation> operations = new ArrayList<>();
    			
    	Operation op1 =
		Operation
		.builder()
		.code(10)
		.machineId(machineRepository.findByCode("m").getId())
		.scrappedQuantity(0)
		.producedQuantity(0)
		.state(OperationState.NEW)
		.orderId(orderRepository.findByCode(1L).getId())
		.build();
    			
    	Operation op2 =
		Operation
		.builder()
		.code(20)
		.machineId(machineRepository.findByCode("m").getId())
		.scrappedQuantity(0)
		.producedQuantity(0)
		.state(OperationState.NEW)
		.orderId(orderRepository.findByCode(1L).getId())
		.build();
    			
    	operations.add(op2);
    	operations.add(op1);
    	operationRepository.saveAll(operations);
    }
    
    
	
	@Test
	public void operationPerOrderWorks() 
	{
		assertThat(operationRepository.operationsPerOrder(orderRepository.findByCode(1L).getId()).size()).isEqualTo(2);
	} 
	
}
