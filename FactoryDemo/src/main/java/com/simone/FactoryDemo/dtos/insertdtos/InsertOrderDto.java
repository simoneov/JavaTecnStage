package com.simone.FactoryDemo.dtos.insertdtos;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.simone.FactoryDemo.models.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsertOrderDto {

	private Integer requestedQuantity;

	private ZonedDateTime deliveryDate;
	
	private String itemCode;
	
	private Long code;
	
	public Order toModel() 
	{
		return Order
				.builder()
				.code(code)
				.deliveryDate(deliveryDate)
				.requestedQuantity(requestedQuantity)
				.itemId(UUID.fromString(itemCode))
				.producedQuantity(0)
				.scrappedQuantity(0)
				.build();
	}

}
