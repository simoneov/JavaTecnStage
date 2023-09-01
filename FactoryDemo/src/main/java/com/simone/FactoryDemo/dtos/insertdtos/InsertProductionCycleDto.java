package com.simone.FactoryDemo.dtos.insertdtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsertProductionCycleDto {

	private String code;
	
	private String description;
}
