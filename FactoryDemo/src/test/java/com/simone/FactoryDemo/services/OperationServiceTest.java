package com.simone.FactoryDemo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.simone.FactoryDemo.dtos.updatedtos.ProduceQuantityDto;
import com.simone.FactoryDemo.dtos.updatedtos.StartWorkDto;
import com.simone.FactoryDemo.dtos.updatedtos.SuspendWorkDto;
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
import com.simone.FactoryDemo.services.implementations.OperationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class OperationServiceTest {
	@Mock
	private MachineRepository machineRepository;
	@Mock
	private OperationRepository operationRepository;
	@Mock
	private OrderRepository orderRepository;
	@InjectMocks
	private OperationServiceImpl operationService;
	

    

    
    @BeforeEach
    void init() 
    {


    }
    

    
    
    //TESTS FOR THE START WORK METHOD
    
    @Test
    public void startWorkWithoutOperationThrowsException() throws InvalidOperationException 
    {
    	//GIVEN
    	StartWorkDto sw = StartWorkDto.builder().code(null).machineCode(null).orderCode(null).build();
    	when(operationRepository.findByCodeAndOrderCode(any(), any())).thenReturn(null);
    	
    	//WHEN
    	InvalidOperationException ex = assertThrows(InvalidOperationException.class,()->{operationService.startWork(sw);});
    	
    	//THEN
    	assertThat(ex.getMessage()).isEqualTo("Operation not found");
    	verify(operationRepository,times(0)).save(any());

    	assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }
    
    
    
    @Test
    public void startWorkWhenTheMachineIsAlreadyRunningThrowsException() throws InvalidOperationException 
    {
    	//GIVEN
    	StartWorkDto sw = StartWorkDto.builder().code(null).machineCode(null).orderCode(null).build();
    	Operation op = Operation.builder().code(10).description("infornata").durataMin(0).scrappedQuantity(0).scrappedQuantity(0).build();
    	when(operationRepository.findByCodeAndOrderCode(any(), any())).thenReturn(op);
    	
    	
    	when(machineRepository.findByCode(any())).thenReturn(Machine.builder().code("a").description("a").state(MachineState.RUN).build());
    	when(machineRepository.findById(any())).thenReturn(Optional.of(Machine.builder().code("a").description("a").state(MachineState.RUN).build()));
    	
    	//WHEN
    	InvalidOperationException ex = assertThrows(InvalidOperationException.class,()->{operationService.startWork(sw);});
    	
    	//THEN
    	assertThat(ex.getMessage()).isEqualTo("Operation can not start because the order, the machine or the operation are not in the right state");
    	assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    	verify(operationRepository,times(0)).save(any());
    }
    
    

    @Test
    public void withAllTheRightConditionsYouCanStartTheOperation() throws InvalidOperationException 
    {
    	//GIVEN
    	Operation op = Operation.builder().code(10).state(OperationState.NEW).producedQuantity(0)
    			.description("infornata").durataMin(0).scrappedQuantity(0).orderId(UUID.randomUUID()).build();
    	Mockito.lenient().when(operationRepository.findByCodeAndOrderCode(any(), any())).thenReturn(op);
    	
    	
    	Mockito.lenient().when(machineRepository.findByCode(any())).thenReturn(Machine.builder().code("a").description("a").state(MachineState.STOP).build());
    	Mockito.lenient().when(machineRepository.findById(any())).thenReturn(Optional.of(Machine.builder().code("a").description("a").state(MachineState.STOP).build()));
    	Mockito.lenient().when(orderRepository.findByCode(any())).thenReturn(Order.builder().code(1L).producedQuantity(0).scrappedQuantity(0).state(OrderState.INPRODUCTION).build());
    	
    	//WHEN
    	operationService.startWork(StartWorkDto.builder().code(10).orderCode(1L).machineCode("a").build());
    	
    	//THEN
    	verify(operationRepository,times(1)).save(any());
    }
    
    @Test
    public void youCantStartAnOperationWithoutItsMachine() 
    {
    	//GIVEN
    	StartWorkDto sw = StartWorkDto.builder().code(null).machineCode(null).orderCode(null).build();
    	Operation op = Operation.builder().code(10).description("infornata").durataMin(0).scrappedQuantity(0).scrappedQuantity(0).build();
    	when(operationRepository.findByCodeAndOrderCode(any(), any())).thenReturn(op);
    	when(machineRepository.findById(any())).thenReturn(Optional.of(Machine.builder().code("a").description("a").state(MachineState.RUN).build()));
    	when(machineRepository.findByCode(any())).thenReturn(Machine.builder().code("b").description("a").state(MachineState.RUN).build());
    	
    	//WHEN
    	InvalidOperationException ex = assertThrows(InvalidOperationException.class,()->{operationService.startWork(sw);});
    	
    	//THEN
    	assertThat(ex.getMessage()).isEqualTo("The machine  with this code exists, but is not connected to the operation that you want to start");
    	verify(operationRepository,times(0)).save(any());

    }
    
    
    //TESTS FOR THE SUSPENDWORK
    
    @Test
    public void withTheRightConditionsItSuspendTheOperation() throws InvalidOperationException 
    {
    	//GIVEN
    	when(operationRepository.findByCodeAndOrderCode(any(),any())).thenReturn(Operation.builder().code(10)
    			.description("infornata").durataMin(0).state(OperationState.INPROGRESS)
    			.scrappedQuantity(0).scrappedQuantity(0).build());
    	when(machineRepository.findById(any()))
    	.thenReturn(Optional.of(Machine.builder().code("d").description("d").state(MachineState.STOP).build()));
    	
    	//WHEN
    	operationService.suspendWork(new SuspendWorkDto(1,3L));
    	
    	//THEN
    	verify(operationRepository,times(1)).save(any());

    }
    
    
    @Test
    public void withOperationCompletedYouCantSuspendTheOperation() throws InvalidOperationException 
    {
    	//GIVEN
    	when(operationRepository.findByCodeAndOrderCode(any(),any())).thenReturn(Operation.builder().code(10)
    			.description("infornata").durataMin(0).state(OperationState.COMPLETED)
    			.scrappedQuantity(0).scrappedQuantity(0).build());
    	Mockito.lenient().when(machineRepository.findById(any()))
    	.thenReturn(Optional.of(Machine.builder().code("d").description("d").state(MachineState.STOP).build()));
    	
    	//WHEN
    	InvalidOperationException ex = assertThrows(InvalidOperationException.class,
    			()->{operationService.suspendWork(new SuspendWorkDto(1,3L));});
    	
    	//THEN
    	assertThat(ex.getMessage()).isEqualTo("You can't suspend an operation that is not running");
    	verify(operationRepository,times(0)).save(any());
    }
    
    
    //TESTS FOR PRODUCEQUANTITY
    
    @Test
    public void youCantProduceMoreThanRequestedPerOrder() throws InvalidOperationException 
    {
    	//GIVEN
    	when(operationRepository.findByCodeAndOrderCode(any(),any())).thenReturn(Operation.builder().code(10)
    			.description("infornata").durataMin(0).producedQuantity(0).state(OperationState.INPROGRESS)
    			.scrappedQuantity(0).scrappedQuantity(0).build());
    	when(orderRepository.findByCode(any()))
    	.thenReturn(Order.builder().code(1L).deliveryDate(ZonedDateTime.now()).producedQuantity(0)
    			.state(OrderState.INPRODUCTION).scrappedQuantity(0)
    			.requestedQuantity(50).build());
    	
    	//WHEN
    	InvalidOperationException ex = assertThrows(InvalidOperationException.class,
    			()->{operationService.produceQuantity(ProduceQuantityDto.builder().code(1)
    					.orderCode(1L).discardedQuantity(50).manufacturedQuantity(500).build());});
    	
    	//THEN
    	assertThat(ex.getMessage()).isEqualTo("The quantity can't surpass the requested quantity in the order");
    	verify(operationRepository,times(0)).save(any());
    }
    
    
    //to verify with mocks that it saves even the order i had to make return a mocked method an operation with an order 
    @Test
    public void underTheRightConditionsProduceQuantityUpdatesEvenTheOrderQuantity() throws InvalidOperationException 
    {
    	
    	//GIVEN
    	Order order = Order.builder().code(1L).deliveryDate(ZonedDateTime.now()).producedQuantity(0)
    			.state(OrderState.INPRODUCTION).scrappedQuantity(0)
    			.requestedQuantity(50).build();
    	
    	when(operationRepository.findByCodeAndOrderCode(any(),any())).thenReturn(Operation.builder().code(10)
    			.description("infornata").durataMin(0).producedQuantity(0).state(OperationState.INPROGRESS)
    			.scrappedQuantity(0).scrappedQuantity(0).order(order).build());
    	
    	
    	when(orderRepository.findByCode(any()))
    	.thenReturn(order);
    	when(operationRepository.lastOrderOperation(any())).thenReturn(10);
    	ArgumentCaptor<Operation> argumentCaptor = ArgumentCaptor.forClass(Operation.class);
    	
    	
    	//WHEN
    	operationService.produceQuantity(ProduceQuantityDto.builder().code(1)
				.orderCode(1L).discardedQuantity(50).manufacturedQuantity(50).build());


    	//THEN
    	verify(operationRepository,times(1)).save(argumentCaptor.capture());
    	assertThat(argumentCaptor.getValue().getOrder().getProducedQuantity()).isEqualTo(50);
    }
    


}
