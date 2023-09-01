package com.simone.FactoryDemo.dtos.updatedtos;


import com.simone.FactoryDemo.models.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemDto {
	
    private String code;
	
	private Integer quantity;
	
	private String description;
	
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
