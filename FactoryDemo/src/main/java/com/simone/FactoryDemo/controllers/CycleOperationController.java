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

import com.simone.FactoryDemo.dtos.insertdtos.InsertCycleOperationDto;
import com.simone.FactoryDemo.dtos.updatedtos.UpdateCycleOperationDto;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.CycleOperation;
import com.simone.FactoryDemo.services.interfaces.CycleOperationService;
import com.simone.FactoryDemo.viewmodels.CycleOperationViewModel;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/cycleoperation")
public class CycleOperationController {
	
	@Autowired
	CycleOperationService cycleOperationService;
	
    @GetMapping
    public List<CycleOperationViewModel> getAll() {
        
        return cycleOperationService.getAll();

    }
    
    @GetMapping(value = "/{productionCycleCode}/{operationCode}")
    public CycleOperation getById(@PathVariable String productionCycleCode, @PathVariable Integer operationCode) {
        
     	return cycleOperationService.findByProductionCycleAndOperationCode(productionCycleCode, operationCode);

    }
    
    @PostMapping
    public void create(@RequestBody InsertCycleOperationDto insertCycleOperation) 
    {
    	
    	try {
    		cycleOperationService.create(insertCycleOperation);
		} catch (InvalidOperationException e) {
			throw new ResponseStatusException(e.getStatusCode(),e.getMessage(),e);
		}
    	
    }
    
    
    @Transactional
    @DeleteMapping("/{productionCycleCode}/{operationCode}")
    public void delete(@PathVariable String productionCycleCode, @PathVariable Integer operationCode) 
    {
    	
    	cycleOperationService.delete(productionCycleCode,operationCode);
    	
    }
    
    @PutMapping
    public void update(@RequestBody UpdateCycleOperationDto cycleOperation) 
    {
    	
    	try {
			cycleOperationService.update(cycleOperation);
		} catch (InvalidOperationException e) {
			throw new ResponseStatusException(e.getStatusCode(),e.getMessage(),e);
		}
    	
    }

}
