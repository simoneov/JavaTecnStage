package com.simone.FactoryDemo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.simone.FactoryDemo.dtos.insertdtos.GenerateOrderWithOperationsDto;
import com.simone.FactoryDemo.dtos.insertdtos.InsertOrderDto;
import com.simone.FactoryDemo.enums.DurationType;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.CycleOperation;
import com.simone.FactoryDemo.models.Item;
import com.simone.FactoryDemo.models.Operation;
import com.simone.FactoryDemo.repositories.CycleOperationRepository;
import com.simone.FactoryDemo.repositories.ItemRepository;
import com.simone.FactoryDemo.repositories.OperationRepository;
import com.simone.FactoryDemo.repositories.OrderRepository;
import com.simone.FactoryDemo.services.implementations.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

	@Mock
	OrderRepository orderRepository;
	@Mock
	OperationRepository operationRepository;
	@Mock
	ItemRepository itemRepository;
	@Mock
	CycleOperationRepository cycleOperationRepository;
	@InjectMocks
	OrderServiceImpl orderService;
	
	//CREATE TESTS
	
	
	@Test
	public void cantInsertAnOrderWithADateBeforeNow() 
	{
		//GIVEN
		ZonedDateTime now = ZonedDateTime.now();
		now = now.minusDays(2);
		InsertOrderDto orderToInsert = InsertOrderDto.builder().code(1L).itemCode("4").deliveryDate(now).requestedQuantity(0).build();
		when(itemRepository.findByCode(any())).thenReturn(Item.builder().code("s").quantity(3).build());
		
		//WHEN
		InvalidOperationException ex = assertThrows(InvalidOperationException.class,()->{orderService.create(orderToInsert);});
    	
		//THEN
		assertThat(ex.getMessage()).isEqualTo("The delivery day must not be null and must be after the current date");
    	assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    	verify(orderRepository,times(0)).save(any());

	} 
	
	
	
	@Test
	public void withAllRightConditionsYouCanSaveAnOrder() throws InvalidOperationException 
	{
		//GIVEN
		ZonedDateTime now = ZonedDateTime.now();
		now = now.plusDays(2);
		InsertOrderDto orderToInsert = InsertOrderDto.builder().code(1L).itemCode("b").deliveryDate(now).requestedQuantity(0).build();
		when(itemRepository.findByCode(any())).thenReturn(Item.builder().id(UUID.randomUUID()).code("s").quantity(3).build());
		
		//WHEN
		orderService.create(orderToInsert);
    	
		//THEN
		verify(orderRepository,times(1)).save(any());

	} 
	
	
	//GENERATE TESTS
	@Test
	public void underRightConditionsTheMethodThatGeneratesAnOrderWithOperationsDoesWork() throws InvalidOperationException 
	{
		
		//GIVEN
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, 2);
		Date now = c.getTime();
		

		List<CycleOperation> cycleOperations = List.of(CycleOperation
				.builder()
				.invariantDuration(5)
				.operationCode(4)
				.workingQuantity(6)
				.productionCycleId(UUID.randomUUID())
				.type(DurationType.INV)
				.build(),
				CycleOperation
				.builder()
				.invariantDuration(5)
				.operationCode(40)
				.workingQuantity(6)
				.productionCycleId(UUID.randomUUID())
				.type(DurationType.INV)
				.build());
		
		GenerateOrderWithOperationsDto generateOrderDto = GenerateOrderWithOperationsDto
				.builder()
				.deliveryDate(now)
				.quantity(56)
				.itemCode("s")
				.build(); 
		when(itemRepository.findByCode(any()))
		.thenReturn(Item.builder().code("s").productionCycleId(UUID.randomUUID()).quantity(3).build());
		when(orderRepository.lastOrder()).thenReturn(1L);
		when(cycleOperationRepository.findAllCycleOperationsByCycle(any())).thenReturn(cycleOperations);

		@SuppressWarnings("unchecked")
		ArgumentCaptor<List<Operation>> argumentCaptorOperation = ArgumentCaptor.forClass((List.class));
    	
    	//WHEN
    	orderService.generateOrderWithOperations(generateOrderDto);
    	
    	//THEN
    	verify(orderRepository,times(1)).save(any());
    	verify(operationRepository,times(1)).saveAll(argumentCaptorOperation.capture());
    	assertThat(argumentCaptorOperation.getValue().size()).isEqualTo(2);
	}
	

}
