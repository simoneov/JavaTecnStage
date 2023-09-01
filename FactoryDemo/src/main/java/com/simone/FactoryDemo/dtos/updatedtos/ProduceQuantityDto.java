package com.simone.FactoryDemo.dtos.updatedtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProduceQuantityDto {

	private Integer code;
	
	private Long orderCode;
	
	private Integer manufacturedQuantity;
	
	private Integer discardedQuantity;
	
}
