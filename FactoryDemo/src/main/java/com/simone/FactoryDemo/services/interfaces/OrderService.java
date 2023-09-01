package com.simone.FactoryDemo.services.interfaces;

import java.util.List;
import org.springframework.stereotype.Service;

import com.simone.FactoryDemo.dtos.insertdtos.GenerateOrderWithOperationsDto;
import com.simone.FactoryDemo.dtos.insertdtos.InsertOrderDto;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.Order;

@Service
public interface OrderService {
	
	public Order findByCode(Long code);
	public List<Order> getAll(); 
	public void update(Order order);
	public void create(InsertOrderDto order) throws InvalidOperationException;
	public void delete(Long code);
	public void endOrder(Long code) throws InvalidOperationException;
	
	public void generateOrderWithOperations(GenerateOrderWithOperationsDto generateOrderDto) throws InvalidOperationException;

}
