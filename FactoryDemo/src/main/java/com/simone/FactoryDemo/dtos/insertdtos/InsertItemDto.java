package com.simone.FactoryDemo.dtos.insertdtos;



import com.simone.FactoryDemo.models.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsertItemDto {

	private String code;
	
	private Integer quantity;
	
	private String description;
	
	private String productionCycleCode;
	
	public Item toModel() 
	{
		return Item
				.builder()
				.code(code)
				.description(description)
				.quantity(quantity)
				.build();
	}
	
}
