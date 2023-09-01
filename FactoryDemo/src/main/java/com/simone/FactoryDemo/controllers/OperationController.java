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

import com.simone.FactoryDemo.dtos.insertdtos.InsertOperationDto;
import com.simone.FactoryDemo.dtos.updatedtos.StartWorkDto;
import com.simone.FactoryDemo.dtos.updatedtos.SuspendWorkDto;
import com.simone.FactoryDemo.dtos.updatedtos.ProduceQuantityDto;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.Operation;
import com.simone.FactoryDemo.services.interfaces.OperationService;
import com.simone.FactoryDemo.viewmodels.OperationViewModel;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/operation")
public class OperationController {

	@Autowired
	OperationService operationService;

	@GetMapping
	public List<OperationViewModel> getAll() {

		return operationService.getAll();

	}

	
	@GetMapping(value = "/{code}/{orderCode}")
	public Operation getByCodeAndOrderCode(@PathVariable Integer code, @PathVariable Long orderCode) {

		return operationService.findByCodeAndOrderCode(code,orderCode);

	}

	@PostMapping
	public void create(@RequestBody InsertOperationDto insertOperation) {

		try {
		operationService.create(insertOperation);
		}catch(InvalidOperationException e) {
			throw new ResponseStatusException(e.getStatusCode(),e.getMessage(),e);
		}
	}

	
	@Transactional
	@DeleteMapping("/{code}/{orderCode}")
	public void delete(@PathVariable Integer code, @PathVariable Long orderCode) {

		operationService.delete(code, orderCode);

	}

	@PutMapping("/startwork")
	public void startWork(@RequestBody StartWorkDto updateOperation) {

		try 
		{
			operationService.startWork(updateOperation);
		}catch(InvalidOperationException e) {
			throw new ResponseStatusException(e.getStatusCode(),e.getMessage(),e);
		}
	}

	
	@PutMapping("/suspendwork")
	public void suspendWork(@RequestBody SuspendWorkDto updateOperation) {

		try 
		{
			operationService.suspendWork(updateOperation);
		}catch(InvalidOperationException e) {
			throw new ResponseStatusException(e.getStatusCode(),e.getMessage(),e);
		}
	}
	
	@PutMapping("/producequantity")
	public void produceQuantity(@RequestBody ProduceQuantityDto updateOperation) {

		try 
		{
			operationService.produceQuantity(updateOperation);
		}catch(InvalidOperationException e) {
			throw new ResponseStatusException(e.getStatusCode(),e.getMessage(),e);
		}
	}
	
	@PutMapping("/end/{code}/{orderCode}")
	public void endOperation(@PathVariable Integer code, @PathVariable Long orderCode) {

		try 
		{
			operationService.endOperation(code, orderCode);
		}catch(InvalidOperationException e) {
			throw new ResponseStatusException(e.getStatusCode(),e.getMessage(),e);
		}
	}
	
	
}
