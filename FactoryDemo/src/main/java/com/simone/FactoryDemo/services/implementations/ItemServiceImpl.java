package com.simone.FactoryDemo.services.implementations;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.simone.FactoryDemo.dtos.insertdtos.InsertItemDto;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.Item;
import com.simone.FactoryDemo.repositories.ItemRepository;
import com.simone.FactoryDemo.repositories.ProductionCycleRepository;
import com.simone.FactoryDemo.services.interfaces.ItemService;

@Service
public class ItemServiceImpl implements ItemService{

	ItemRepository itemRepository;
	ProductionCycleRepository productionCycleRepository;
	
	public ItemServiceImpl(ItemRepository itemRepository, ProductionCycleRepository productionCycleRepository)
	{
		this.itemRepository = itemRepository;
		this.productionCycleRepository = productionCycleRepository;
	}
	
	public Item findByCode(String code)
	{
		Item item = itemRepository.findByCode(code);
		return item;
	}
	
	public List<Item> getAll()
	{
		return itemRepository.findAll();
	} 
	
	public void update(Item item) 
	{
		Item itemToUpdate = itemRepository
				.findByCode(item.getCode());

		if(itemToUpdate != null) 
		{
			itemToUpdate.setDescription(item.getDescription());
			itemToUpdate.setQuantity(item.getQuantity());
			itemRepository.save(itemToUpdate);
		}
		
	}
	
	public void create(InsertItemDto insertItem) throws InvalidOperationException 
	{
		if(insertItem.getQuantity()<0)
			throw new InvalidOperationException("The quantity inserted has to be equal or greater than 0",HttpStatus.BAD_REQUEST);
		Item itemToSave = insertItem.toModel();
		itemToSave.setProductionCycleId(productionCycleRepository.findByCode(insertItem.getProductionCycleCode()).getId());
		itemRepository.save(itemToSave);
	}
	
	public void delete(String code) 
	{
		itemRepository.removeByCode(code);
	}

}
