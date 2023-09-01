package com.simone.FactoryDemo.services.implementations;

import java.util.List;
import org.springframework.stereotype.Service;

import com.simone.FactoryDemo.models.Machine;
import com.simone.FactoryDemo.repositories.MachineRepository;
import com.simone.FactoryDemo.services.interfaces.MachineService;

@Service
public class MachineServiceImpl implements MachineService{
	
	MachineRepository machineRepository;
	
	public MachineServiceImpl(MachineRepository machineRepository)
	{
		this.machineRepository = machineRepository;
	}
	
	public Machine findByCode(String code)
	{
		Machine machine = machineRepository.findByCode(code);
		return machine;
	}
	
	public List<Machine> getAll()
	{
		return machineRepository.findAll();
	} 
	
	public void update(String code, Machine machine) 
	{
		Machine machineToUpdate = machineRepository
				.findByCode(code);

		if(machineToUpdate != null) 
		{
			machineToUpdate.setDescription(machine.getDescription());
			machineToUpdate.setState(machine.getState());
			machineRepository.save(machineToUpdate);
		}
		
	}
	
	public void create(Machine insertMachine) 
	{
			machineRepository.save(insertMachine);
		
	}
	
	public void delete(String code) 
	{
		machineRepository.removeByCode(code);
	}


	


	
}
