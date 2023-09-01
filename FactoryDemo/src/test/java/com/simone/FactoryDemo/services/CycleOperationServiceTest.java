package com.simone.FactoryDemo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simone.FactoryDemo.dtos.insertdtos.InsertCycleOperationDto;
import com.simone.FactoryDemo.dtos.updatedtos.UpdateCycleOperationDto;
import com.simone.FactoryDemo.enums.DurationType;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.CycleOperation;
import com.simone.FactoryDemo.models.ProductionCycle;
import com.simone.FactoryDemo.repositories.CycleOperationRepository;
import com.simone.FactoryDemo.repositories.MachineRepository;
import com.simone.FactoryDemo.repositories.ProductionCycleRepository;
import com.simone.FactoryDemo.services.implementations.CycleOperationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CycleOperationServiceTest {

	@Mock
	CycleOperationRepository cycleOperationRepository;
	@Mock
	ProductionCycleRepository productionCycleRepository;
	@Mock
	MachineRepository machineRepository;
	@InjectMocks
	CycleOperationServiceImpl cycleOperationServiceImpl;
	
	
	//CREATE TESTS
	@Test
	public void createInsertsInvariantTypeRight() throws InvalidOperationException 
	{
		//GIVEN
		InsertCycleOperationDto cycleOperation = InsertCycleOperationDto
				.builder()
				.description("test")
				.type(DurationType.INV)
				.workingQuantity(6)
				.workingTime(2)
				.invariantDuration(5)
				.productionCycleCode("blabla")
				.build();
    	ArgumentCaptor<CycleOperation> argumentCaptor = ArgumentCaptor.forClass(CycleOperation.class);
    	when(machineRepository.findByCode(any())).thenReturn(null);
    	when(productionCycleRepository.findByCode(any()))
    	.thenReturn(ProductionCycle.builder().code("blabla").description(null).build());
    	
    	//WHEN
    	cycleOperationServiceImpl.create(cycleOperation);
    	
    	
    	//THEN
    	verify(cycleOperationRepository,times(1)).save(argumentCaptor.capture());
    	assertThat(argumentCaptor.getValue().getInvariantDuration()).isEqualTo(5);
    	assertThat(argumentCaptor.getValue().getWorkingTime()).isEqualTo(null);
    	assertThat(argumentCaptor.getValue().getWorkingQuantity()).isEqualTo(null);
    	
	}
	
	
	@Test
	public void createInsertsVariantTypeRight() throws InvalidOperationException 
	{
		//GIVEN
		InsertCycleOperationDto cycleOperation = InsertCycleOperationDto
				.builder()
				.description("test")
				.type(DurationType.VAR)
				.workingQuantity(6)
				.workingTime(2)
				.invariantDuration(5)
				.productionCycleCode("blabla")
				.build();
    	ArgumentCaptor<CycleOperation> argumentCaptor = ArgumentCaptor.forClass(CycleOperation.class);
    	when(machineRepository.findByCode(any())).thenReturn(null);
    	when(productionCycleRepository.findByCode(any()))
    	.thenReturn(ProductionCycle.builder().code("blabla").description(null).build());
    	
    	//WHEN
    	cycleOperationServiceImpl.create(cycleOperation);
    	
    	
    	//THEN
    	verify(cycleOperationRepository,times(1)).save(argumentCaptor.capture());
    	assertThat(argumentCaptor.getValue().getInvariantDuration()).isEqualTo(null);
    	assertThat(argumentCaptor.getValue().getWorkingTime()).isEqualTo(2);
    	assertThat(argumentCaptor.getValue().getWorkingQuantity()).isEqualTo(6);
    	
	}
	
	
	//UPDATE TESTS
	@Test
	public void withTheRightConditionsItUpdatesTheVariantQuantities() throws InvalidOperationException 
	{
		//GIVEN
		UpdateCycleOperationDto cycleOperation = UpdateCycleOperationDto
				.builder()
				.description("test")
				.type(DurationType.VAR)
				.workingQuantity(6)
				.workingTime(2)
				.invariantDuration(5)
				.productionCycleCode("blabla")
				.build();
		
		when(cycleOperationRepository
		.findByProductionCycleAndOperationCode(any(),any()))
		.thenReturn(CycleOperation.builder().description(null).invariantDuration(null)
				.workingQuantity(0).workingTime(null).productionCycleId(UUID.randomUUID()).operationCode(3).build());
		
    	ArgumentCaptor<CycleOperation> argumentCaptor = ArgumentCaptor.forClass(CycleOperation.class);
    	when(machineRepository.findByCode(any())).thenReturn(null);
    	Mockito.lenient().when(productionCycleRepository.findByCode(any()))
    	.thenReturn(ProductionCycle.builder().code("blabla").description(null).build());
    	
    	//WHEN
    	cycleOperationServiceImpl.update(cycleOperation);
    	
    	
    	//THEN
    	verify(cycleOperationRepository,times(1)).save(argumentCaptor.capture());
    	assertThat(argumentCaptor.getValue().getInvariantDuration()).isEqualTo(null);
    	assertThat(argumentCaptor.getValue().getWorkingTime()).isEqualTo(2);
    	assertThat(argumentCaptor.getValue().getWorkingQuantity()).isEqualTo(6);
	}
	
	
	
	@Test
	public void withDuratioTypeNullItUpdatesOnlyRightQuantities() throws InvalidOperationException 
	{
		//GIVEN
		UpdateCycleOperationDto cycleOperation = UpdateCycleOperationDto
				.builder()
				.description("test")
				.type(null)
				.workingQuantity(6)
				.workingTime(2)
				.invariantDuration(5)
				.productionCycleCode("blabla")
				.build();
		
		CycleOperation toUpdate = CycleOperation.builder().description(null).type(DurationType.VAR).invariantDuration(2)
				.workingQuantity(43).workingTime(null).productionCycleId(UUID.randomUUID()).operationCode(3).build();
		when(cycleOperationRepository
		.findByProductionCycleAndOperationCode(any(),any()))
		.thenReturn(toUpdate);
		
    	ArgumentCaptor<CycleOperation> argumentCaptor = ArgumentCaptor.forClass(CycleOperation.class);
    	when(machineRepository.findByCode(any())).thenReturn(null);
    	Mockito.lenient().when(productionCycleRepository.findByCode(any()))
    	.thenReturn(ProductionCycle.builder().code("blabla").description(null).build());
    	
    	//WHEN
    	cycleOperationServiceImpl.update(cycleOperation);
    	
    	
    	//THEN
    	assertThat(toUpdate.getInvariantDuration()).isEqualTo(null);
    	verify(cycleOperationRepository,times(1)).save(argumentCaptor.capture());
    	assertThat(argumentCaptor.getValue().getInvariantDuration()).isEqualTo(null);
    	assertThat(argumentCaptor.getValue().getWorkingTime()).isEqualTo(2);
    	assertThat(argumentCaptor.getValue().getWorkingQuantity()).isEqualTo(6);
	}
	
	
	
}
