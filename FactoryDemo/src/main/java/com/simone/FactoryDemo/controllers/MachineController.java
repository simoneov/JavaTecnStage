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

import com.simone.FactoryDemo.dtos.insertdtos.InsertMachineDto;
import com.simone.FactoryDemo.dtos.updatedtos.UpdateMachineDto;
import com.simone.FactoryDemo.models.Machine;
import com.simone.FactoryDemo.services.interfaces.MachineService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/machine")
public class MachineController {
	
	@Autowired
	MachineService machineService;

    @GetMapping
    public List<Machine> getAll() {
        
        return machineService.getAll();

    }
    
    @GetMapping(value = "/{machineId}")
    public Machine getById(@PathVariable String machineId) 
    {
                
     	return machineService.findByCode(machineId);

    }
    
    @PostMapping
    public void create(@RequestBody InsertMachineDto insertMachine) 
    {

    	machineService.create(insertMachine.toModel());
    	
    }
    
    @Transactional
    @DeleteMapping("/{code}")
    public void delete(@PathVariable String code) 
    {
    	
    	machineService.delete(code);;
    	
    }
    
    @PutMapping("/{code}")
    public void update(@PathVariable String code, @RequestBody UpdateMachineDto updateMachine) 
    {
    	
    	machineService.update(code, updateMachine.toModel());
    	
    }
    
}
