package com.simone.FactoryDemo.dtos.updatedtos;

import java.time.ZonedDateTime;

import com.simone.FactoryDemo.enums.OrderState;
import com.simone.FactoryDemo.models.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderDto {
	
	private Long code;

	private Integer manufacturedQuantity;

	private Integer discardedQuantity;
	
	private String state;

	private ZonedDateTime startDate;

	private ZonedDateTime endDate;

	public Order toModel() 
	{
		return Order
				.builder()
				.code(code)
				.producedQuantity(manufacturedQuantity)
				.scrappedQuantity(discardedQuantity)
				.state(OrderState.valueOf(state))
				.startDate(startDate)
				.endDate(endDate)
				.build();
	}

	
}
