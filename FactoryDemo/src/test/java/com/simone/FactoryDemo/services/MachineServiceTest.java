package com.simone.FactoryDemo.services;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.simone.FactoryDemo.models.Machine;
import com.simone.FactoryDemo.repositories.MachineRepository;
import com.simone.FactoryDemo.services.implementations.MachineServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MachineServiceTest {

	@Mock
	private MachineRepository machineRepository;
	@InjectMocks
	private MachineServiceImpl machineService;
	

    
    private Machine machine;
    private List<Machine> machines;
    
    @BeforeEach
    void init() {

        machine = Machine.builder().code("q").description("mach").build();

        machines = new ArrayList<>();
        machines.add(machine);
    }
    
    @Test
    public void machineIsCreatedTest() 
    {

    	//GIVEN
    	when(machineRepository.save(machine)).thenReturn(machine);
    	
    	//WHEN
    	machineService.create(machine);
    	
    	//THEN
    	verify(machineRepository,times(1)).save(machine);
    }


    @Test
    public void machineIsReturned() 
    {
    	//GIVEN
    	when(machineRepository.findByCode("q")).thenReturn(machine);
    	
    	//WHEN
    	Machine testMachine = machineService.findByCode("q");
    	
    	//THEN
    	assertThat("q").isEqualTo(testMachine.getCode());
    	
    }
    
}
