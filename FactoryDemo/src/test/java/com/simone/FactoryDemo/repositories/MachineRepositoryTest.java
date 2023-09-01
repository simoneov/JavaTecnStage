package com.simone.FactoryDemo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.simone.FactoryDemo.enums.MachineState;
import com.simone.FactoryDemo.models.Machine;

@DataJpaTest
public class MachineRepositoryTest {

	@Autowired
	MachineRepository machineRepository;
	
	Machine machine;
	List<Machine> machines;
	
    @BeforeEach
    void init() {

        machine = Machine.builder().code("q").description("mach").state(MachineState.STOP).build();

        machines = new ArrayList<>();
        machines.add(machine);
    }
	
	@Test
	public void findByCodeWorks() 
	{
		//WHEN
		machineRepository.save(machine);
		Machine testMachine = machineRepository.findByCode("q");
		
		//THEN
		assertThat(testMachine.getCode()).isEqualTo("q");
	} 
}
