package com.simone.FactoryDemo.viewmodels;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationViewModel {

	
    private Integer code;
	
	private String description;
	
	private String state; 

	private Integer producedQuantity;
	
	private Integer scrappedQuantity;
	
	private Date startDate;
	
	private Integer durataMin;
	
	private Date endDate;
		
	private Long orderCode;
	
    private String machineCode;
	
	
}
