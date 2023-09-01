package com.simone.FactoryDemo.services.interfaces;

import java.util.List;
import org.springframework.stereotype.Service;

import com.simone.FactoryDemo.dtos.insertdtos.InsertItemDto;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.Item;

@Service
public interface ItemService {
	
	public Item findByCode(String code);
	public List<Item> getAll(); 
	public void update(Item item);
	public void create(InsertItemDto insertItem) throws InvalidOperationException;
	public void delete(String code);
}
