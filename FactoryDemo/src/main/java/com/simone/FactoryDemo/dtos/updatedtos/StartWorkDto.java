package com.simone.FactoryDemo.dtos.updatedtos;

import com.simone.FactoryDemo.enums.OperationState;
import com.simone.FactoryDemo.models.Operation;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartWorkDto {
	
	private String machineCode;
	
	private Integer code;
	
	private Long orderCode;
			
	public Operation toModel() 
	{
		return Operation
				.builder()
				.code(code)
				.state(OperationState.INPROGRESS)
				.build();
	}
	

}
