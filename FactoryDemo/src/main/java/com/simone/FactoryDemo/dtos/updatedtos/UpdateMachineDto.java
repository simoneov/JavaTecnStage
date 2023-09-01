package com.simone.FactoryDemo.dtos.updatedtos;



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
public class UpdateMachineDto {

	
	
	private String state;
	
	private String description;

	public Machine toModel() 
	{
		return Machine
				.builder()
				.description(description)
				.state(MachineState.valueOf(state))
				.build();
	}
	
}
