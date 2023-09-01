package com.simone.FactoryDemo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simone.FactoryDemo.repositories.CycleOperationRepository;
import com.simone.FactoryDemo.repositories.ItemRepository;
import com.simone.FactoryDemo.repositories.MachineRepository;
import com.simone.FactoryDemo.repositories.OperationRepository;
import com.simone.FactoryDemo.repositories.OrderRepository;
import com.simone.FactoryDemo.repositories.ProductionCycleRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/delete")
public class DeleteController {

	@Autowired
	ItemRepository itemRepository;
	@Autowired
	MachineRepository machineRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	OperationRepository operationRepository;
	@Autowired
	CycleOperationRepository cycleOperationRepository;
	@Autowired
	ProductionCycleRepository productionCycleRepository;
	
	
    @Transactional
    @DeleteMapping
    public void deleteAllData() 
    {
    	operationRepository.deleteAll();
    	cycleOperationRepository.deleteAll();
    	orderRepository.deleteAll();
    	machineRepository.deleteAll();
    	itemRepository.deleteAll();
       	productionCycleRepository.deleteAll();
    }

}
