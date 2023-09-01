package com.simone.FactoryDemo.dtos.insertdtos;


import com.simone.FactoryDemo.enums.MachineState;
import com.simone.FactoryDemo.models.Machine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsertMachineDto 
{
	private String code;
	
	private String description;
	
	public Machine toModel() 
	{
		return Machine
				.builder()
				.code(code)
				.state(MachineState.STOP)
				.description(description)
				.build();
	}
	
}
