package com.simone.FactoryDemo.integration;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import com.simone.FactoryDemo.dtos.updatedtos.UpdateMachineDto;
import com.simone.FactoryDemo.enums.MachineState;
import com.simone.FactoryDemo.dtos.insertdtos.InsertMachineDto;
import com.simone.FactoryDemo.models.Machine;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)      
public class MachineControllerIntegrationTest {

	@LocalServerPort
	private int port;
	
	@Autowired
	TestRestTemplate testRestTemplate;

	
    @BeforeEach
    void init() 
    {
    	testRestTemplate.delete("http://localhost:" + port +"/delete");
    }	
	

	@Test
	public void youCanInsertAMachine() 
	{
		
		InsertMachineDto machine = InsertMachineDto.builder().code("test").description("test").build();

		testRestTemplate.postForEntity("http://localhost:" + port +"/machine", machine, InsertMachineDto.class);
		
		Machine test = testRestTemplate.getForObject("http://localhost:" + port +"/machine/test", Machine.class);

		assertThat(test.getCode()).isEqualTo("test");
	}
	
	
	@Test
	public void modifyDoesWork() 
	{
		InsertMachineDto machine = InsertMachineDto.builder().code("test").description("test").build();

		testRestTemplate.postForEntity("http://localhost:" + port +"/machine", machine, InsertMachineDto.class);
		
		UpdateMachineDto updateMachine = UpdateMachineDto.builder().state("RUN").description("MOD").build();

		testRestTemplate.put("http://localhost:" + port +"/machine/test", updateMachine, UpdateMachineDto.class);
		
		Machine test = testRestTemplate.getForObject("http://localhost:" + port +"/machine/test", Machine.class);

		assertThat(test.getDescription()).isEqualTo("MOD");
		
		assertThat(test.getState()).isEqualTo(MachineState.RUN);
		
	}
	
	
}
