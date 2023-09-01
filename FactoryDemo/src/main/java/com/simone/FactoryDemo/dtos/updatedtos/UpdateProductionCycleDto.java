package com.simone.FactoryDemo.dtos.updatedtos;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductionCycleDto {

	private String code;
	
	private String description;
	 
}
