package com.simone.FactoryDemo.dtos.updatedtos;


import com.simone.FactoryDemo.enums.DurationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCycleOperationDto {

	private String description;
	
	private Integer operationCode;
	
	private DurationType type; 

	private Integer workingQuantity;
	
	private Integer workingTime;
	
	private Integer invariantDuration;
	
	private String machineCode;
	
	private String productionCycleCode;
	
}
