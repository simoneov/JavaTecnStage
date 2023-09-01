package com.simone.FactoryDemo.dtos.insertdtos;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateOrderWithOperationsDto {

	private String itemCode;
	private Integer quantity;
	private Date deliveryDate;
	
}
