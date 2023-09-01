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

import com.simone.FactoryDemo.dtos.insertdtos.InsertItemDto;
import com.simone.FactoryDemo.dtos.updatedtos.UpdateItemDto;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.Item;
import com.simone.FactoryDemo.services.interfaces.ItemService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/item")
public class ItemController {

	@Autowired
	ItemService itemService;

    @GetMapping
    public List<Item> getAll() {
        
        return itemService.getAll();

    }
    
    @GetMapping(value = "/{itemCode}")
    public Item getById(@PathVariable String itemCode) {
        
     	return itemService.findByCode(itemCode);

    }
    
    @PostMapping
    public void create(@RequestBody InsertItemDto insertItem) 
    {
    	
    	try {
			itemService.create(insertItem);
		} catch (InvalidOperationException e) {
			throw new ResponseStatusException(e.getStatusCode(),e.getMessage(),e);
		}
    	
    }
    
    
    @Transactional
    @DeleteMapping("/{code}")
    public void delete(@PathVariable String code) 
    {
    	
    	itemService.delete(code);;
    	
    }
    
    @PutMapping
    public void update(@RequestBody UpdateItemDto updateItem) 
    {
    	
    	itemService.update(updateItem.toModel());
    	
    }

}
