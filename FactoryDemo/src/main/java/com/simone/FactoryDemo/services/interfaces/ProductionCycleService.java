package com.simone.FactoryDemo.services.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

import com.simone.FactoryDemo.dtos.insertdtos.InsertProductionCycleDto;
import com.simone.FactoryDemo.dtos.updatedtos.UpdateProductionCycleDto;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.ProductionCycle;

@Service
public interface ProductionCycleService {

	public void create(InsertProductionCycleDto productionCycle) throws InvalidOperationException;
	public void delete(String code);
	public ProductionCycle findByCode(String code);
	public List<ProductionCycle> getAll(); 
	public void update(UpdateProductionCycleDto productionCycle) throws InvalidOperationException;


}
