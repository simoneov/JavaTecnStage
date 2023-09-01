package com.simone.FactoryDemo.services.interfaces;

import java.util.List;
import org.springframework.stereotype.Service;

import com.simone.FactoryDemo.models.Machine;

@Service
public interface MachineService {
	
	public Machine findByCode(String code);
	public List<Machine> getAll(); 
	public void update(String code, Machine machine);
	public void create(Machine machine);
	public void delete(String code);
	
}
