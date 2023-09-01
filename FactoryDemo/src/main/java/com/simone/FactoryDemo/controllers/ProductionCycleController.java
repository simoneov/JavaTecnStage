package com.simone.FactoryDemo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.simone.FactoryDemo.dtos.insertdtos.InsertProductionCycleDto;
import com.simone.FactoryDemo.dtos.updatedtos.UpdateProductionCycleDto;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.ProductionCycle;
import com.simone.FactoryDemo.services.interfaces.ProductionCycleService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/productioncycle")
public class ProductionCycleController {

	@Autowired
	ProductionCycleService productionCycleService;

	@GetMapping
	public List<ProductionCycle> getAll() {

		return productionCycleService.getAll();

	}

	
	@GetMapping(value = "/{code}")
	public ProductionCycle getByCode(@PathVariable String code) {

		return productionCycleService.findByCode(code);

	}

	@PostMapping
	public void create(@RequestBody InsertProductionCycleDto productionCycle) {

		try {
			productionCycleService.create(productionCycle);
		}catch(InvalidOperationException e) {
			throw new ResponseStatusException(e.getStatusCode(),e.getMessage(),e);
		}

	}

	
	@Transactional
	@DeleteMapping("/{code}")
	public void delete(@PathVariable String code) {

		productionCycleService.delete(code);

	}
	
	
	@PutMapping
	public void update(@RequestBody UpdateProductionCycleDto productionCycle) {

		try {
			productionCycleService.update(productionCycle);
		}catch(InvalidOperationException e) {
			throw new ResponseStatusException(e.getStatusCode(),e.getMessage(),e);
		}
	}


}
