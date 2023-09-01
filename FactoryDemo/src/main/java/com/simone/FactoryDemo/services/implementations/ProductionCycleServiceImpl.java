package com.simone.FactoryDemo.services.implementations;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.simone.FactoryDemo.dtos.insertdtos.InsertProductionCycleDto;
import com.simone.FactoryDemo.dtos.updatedtos.UpdateProductionCycleDto;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.ProductionCycle;
import com.simone.FactoryDemo.repositories.ProductionCycleRepository;
import com.simone.FactoryDemo.services.interfaces.ProductionCycleService;

@Service
public class ProductionCycleServiceImpl implements ProductionCycleService{

	ProductionCycleRepository productionCycleRepository;
	
	public ProductionCycleServiceImpl(ProductionCycleRepository productionCycleRepository) 
	{
		this.productionCycleRepository = productionCycleRepository;
	}
	
	@Override
	public void create(InsertProductionCycleDto productionCycle) throws InvalidOperationException {

		if(productionCycle.getCode() == null)
			throw new InvalidOperationException("You have to insert a unique code for the production cycle", HttpStatus.BAD_REQUEST);
		productionCycleRepository.save(ProductionCycle.builder()
				.code(productionCycle.getCode())
				.description(productionCycle.getDescription())
				.build());
	}

	@Override
	public void delete(String code) {
		productionCycleRepository.removeByCode(code);
		
	}

	@Override
	public ProductionCycle findByCode(String code) {
		return productionCycleRepository.findByCode(code);
	}

	@Override
	public List<ProductionCycle> getAll() {
		return productionCycleRepository.findAll();
	}

	@Override
	public void update(UpdateProductionCycleDto productionCycle) throws InvalidOperationException {
		if(productionCycle.getCode()==null)
			throw new InvalidOperationException("You have to insert a unique code for the production cycle that you want modify", HttpStatus.BAD_REQUEST);
		ProductionCycle productionCycleToUpdate = productionCycleRepository.findByCode(productionCycle.getCode());
		productionCycleToUpdate.setDescription(productionCycle.getDescription());
		productionCycleRepository.save(productionCycleToUpdate);
	}

}
