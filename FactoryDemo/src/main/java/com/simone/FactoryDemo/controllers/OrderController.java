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

import com.simone.FactoryDemo.dtos.insertdtos.GenerateOrderWithOperationsDto;
import com.simone.FactoryDemo.dtos.insertdtos.InsertOrderDto;
import com.simone.FactoryDemo.dtos.updatedtos.UpdateOrderDto;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.Order;
import com.simone.FactoryDemo.services.interfaces.OrderService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	OrderService orderService;

	@GetMapping
	public List<Order> getAll() {

		return orderService.getAll();

	}

	
	@GetMapping(value = "/{code}")
	public Order getByCode(@PathVariable Long code) {

		return orderService.findByCode(code);

	}

	@PostMapping
	public void create(@RequestBody InsertOrderDto insertOrder) {

		try {
			orderService.create(insertOrder);
		}catch(InvalidOperationException e) {
			throw new ResponseStatusException(e.getStatusCode(),e.getMessage(),e);
		}

	}

	
	@Transactional
	@DeleteMapping("/{code}")
	public void delete(@PathVariable Long code) {

		orderService.delete(code);

	}

	@PutMapping
	public void update(@RequestBody UpdateOrderDto updateOrder) {

		orderService.update(updateOrder.toModel());
	}
	

	@PutMapping("end/{code}")
	public void endOrder(@PathVariable Long code) {
		try 
		{
			orderService.endOrder(code);
		}catch(InvalidOperationException e) {
			throw new ResponseStatusException(e.getStatusCode(),e.getMessage(),e);
		}
	}
	
	
	
	@PostMapping("/generate")
	public void generate(@RequestBody GenerateOrderWithOperationsDto orderToGenerate) {
		try 
		{
			orderService.generateOrderWithOperations(orderToGenerate);
		}catch(InvalidOperationException e) {
			throw new ResponseStatusException(e.getStatusCode(),e.getMessage(),e);
		}
	}
	

}
