package com.simone.FactoryDemo.dtos.insertdtos;

import java.util.UUID;

import com.simone.FactoryDemo.models.Operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsertOperationDto {

	private Long orderCode;
	
	private String machineCode;
	
	private Integer code;
	
	private String description;


	
	public Operation toModel() 
	{
		return Operation
				.builder()
				.code(code)
				.machineId(UUID.fromString(machineCode))
				.description(description)
				.durataMin(0)
				.build();
	}
}
