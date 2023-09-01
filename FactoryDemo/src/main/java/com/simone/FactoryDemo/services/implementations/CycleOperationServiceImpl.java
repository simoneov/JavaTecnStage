package com.simone.FactoryDemo.services.implementations;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.simone.FactoryDemo.dtos.insertdtos.InsertCycleOperationDto;
import com.simone.FactoryDemo.dtos.updatedtos.UpdateCycleOperationDto;
import com.simone.FactoryDemo.enums.DurationType;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.CycleOperation;
import com.simone.FactoryDemo.models.Machine;
import com.simone.FactoryDemo.repositories.CycleOperationRepository;
import com.simone.FactoryDemo.repositories.MachineRepository;
import com.simone.FactoryDemo.repositories.ProductionCycleRepository;
import com.simone.FactoryDemo.services.interfaces.CycleOperationService;
import com.simone.FactoryDemo.viewmodels.CycleOperationViewModel;

@Service
public class CycleOperationServiceImpl implements CycleOperationService{
	
	
	CycleOperationRepository cycleOperationRepository;
	ProductionCycleRepository productionCycleRepository;
	MachineRepository machineRepository;
	
	public CycleOperationServiceImpl(CycleOperationRepository cycleOperationRepository,
			ProductionCycleRepository productionCycleRepository, MachineRepository machineRepository) 
	{
		this.cycleOperationRepository = cycleOperationRepository;
		this.productionCycleRepository = productionCycleRepository;
		this.machineRepository = machineRepository;
	}

	@Override
	public void create(InsertCycleOperationDto cycleOperation) throws InvalidOperationException {
		if(cycleOperation.getProductionCycleCode() == null || 
				productionCycleRepository.findByCode(cycleOperation.getProductionCycleCode()) == null)		
			throw new InvalidOperationException("To insert a cycle operation you must first connect it to an existent production cycle", HttpStatus.BAD_REQUEST);
		if(cycleOperation.getType()== null)
			throw new InvalidOperationException("The duration type of the operation must be inserted", HttpStatus.BAD_REQUEST);
		if((cycleOperation.getWorkingQuantity()!= null && cycleOperation.getWorkingQuantity()<0) ||
				(cycleOperation.getWorkingTime() != null && cycleOperation.getWorkingTime()<0) || 
				(cycleOperation.getInvariantDuration() != null && cycleOperation.getInvariantDuration()<0))
			throw new InvalidOperationException("The quantities inserted must not be negative", HttpStatus.BAD_REQUEST);
		Machine machineToInsert = machineRepository.findByCode(cycleOperation.getMachineCode());
		if(cycleOperation.getType() == DurationType.INV)
			
		cycleOperationRepository.save(CycleOperation
				.builder()
				.invariantDuration(cycleOperation.getInvariantDuration() == null ? 0 : cycleOperation.getInvariantDuration())
				.type(DurationType.INV)
				.description(cycleOperation.getDescription())
				.operationCode(cycleOperation.getOperationCode() == null ? 0 : cycleOperation.getOperationCode())
				.productionCycleId(productionCycleRepository.findByCode(cycleOperation.getProductionCycleCode()).getId())
				.machineId(machineToInsert == null ? null : machineToInsert.getId())
				.build());
		else
		cycleOperationRepository.save(CycleOperation
				.builder()
				.workingQuantity(cycleOperation.getWorkingQuantity() == null ? 0 : cycleOperation.getWorkingQuantity())
				.workingTime(cycleOperation.getWorkingTime() == null ? 0 : cycleOperation.getWorkingTime())
				.type(DurationType.VAR)
				.description(cycleOperation.getDescription())
				.operationCode(cycleOperation.getOperationCode() == null ? 0 : cycleOperation.getOperationCode())
				.productionCycleId(productionCycleRepository.findByCode(cycleOperation.getProductionCycleCode()).getId())
				.machineId(machineToInsert == null ? null : machineToInsert.getId())
				.build());
		
	}

	@Override
	public void delete(String productionCycleCode, Integer operationCode) {
		cycleOperationRepository.removeByProductionCycleAndOperationCode(productionCycleCode, operationCode);
		
	}

	
	@Override
	public CycleOperation findByProductionCycleAndOperationCode(String productionCycleCode, Integer operationCode) {
		return cycleOperationRepository.findByProductionCycleAndOperationCode(productionCycleCode, operationCode);
	}

	@Override
	public List<CycleOperationViewModel> getAll() {
		return cycleOperationRepository.findAll()
				.stream()
				.map(a -> CycleOperationViewModel
						.builder()
						.operationCode(a.getOperationCode())
						.description(a.getDescription())
						.type(a.getType())
						.invariantDuration(a.getInvariantDuration())
						.workingQuantity(a.getWorkingQuantity())
						.workingTime(a.getWorkingTime())
						.machineCode(machineRepository.findById(a.getMachineId()).get().getCode())
						.productionCycleCode(productionCycleRepository.findById(a.getProductionCycleId()).get().getCode())
						.build())
				.toList();
	}

	//TODO
	@Override
	public void update(UpdateCycleOperationDto cycleOperation) throws InvalidOperationException 
	{
		CycleOperation cycleOperationToUpdate = cycleOperationRepository
				.findByProductionCycleAndOperationCode(cycleOperation.getProductionCycleCode(),cycleOperation.getOperationCode());
		if(cycleOperationToUpdate == null)
			throw new InvalidOperationException("Operation cycle not found", HttpStatus.NOT_FOUND);
		if((cycleOperation.getWorkingTime() != null && cycleOperation.getWorkingTime()<0) ||
				(cycleOperation.getWorkingQuantity() != null && cycleOperation.getWorkingQuantity()<0) ||
				(cycleOperation.getInvariantDuration() != null && cycleOperation.getInvariantDuration()<0))
			throw new InvalidOperationException("Please don't insert negative quantities", HttpStatus.BAD_REQUEST);
		cycleOperationToUpdate.setMachineId(machineRepository.findByCode(cycleOperation.getMachineCode()) == null ?
				null : machineRepository.findByCode(cycleOperation.getMachineCode()).getId());
		cycleOperationToUpdate.setOperationCode(cycleOperation.getOperationCode() == null ?
				cycleOperationToUpdate.getOperationCode() : cycleOperation.getOperationCode());
		if(cycleOperation.getType() == DurationType.INV) 
		{
			cycleOperationToUpdate.setType(cycleOperation.getType());
			cycleOperationToUpdate.setWorkingQuantity(null);
			cycleOperationToUpdate.setWorkingTime(null);
			cycleOperationToUpdate.setInvariantDuration(cycleOperation.getInvariantDuration() != null ?
					cycleOperation.getInvariantDuration() : 0);
		}
		else if(cycleOperation.getType() == DurationType.VAR) 
		{
			cycleOperationToUpdate.setType(cycleOperation.getType());
			cycleOperationToUpdate.setWorkingQuantity(cycleOperation.getWorkingQuantity() != null ?
					cycleOperation.getWorkingQuantity() : 0);
			cycleOperationToUpdate.setWorkingTime(cycleOperation.getWorkingTime() != null ?
					cycleOperation.getWorkingTime() : 0);
			cycleOperationToUpdate.setInvariantDuration(null);
		}
		else if(cycleOperation.getType() == null) 
		{
			if(cycleOperationToUpdate.getType() == DurationType.INV) 
			{
				cycleOperationToUpdate.setWorkingQuantity(null);
				cycleOperationToUpdate.setWorkingTime(null);
				cycleOperationToUpdate.setInvariantDuration(cycleOperation.getInvariantDuration() == null ? cycleOperationToUpdate.getInvariantDuration()
						: cycleOperation.getInvariantDuration());
			}
			else 
			{
				cycleOperationToUpdate.setInvariantDuration(null);
				cycleOperationToUpdate.setWorkingQuantity(cycleOperation.getWorkingQuantity() != null ?
						cycleOperation.getWorkingQuantity() : cycleOperationToUpdate.getWorkingQuantity());
				cycleOperationToUpdate.setWorkingTime(cycleOperation.getWorkingTime() != null ?
						cycleOperation.getWorkingTime() : cycleOperationToUpdate.getWorkingTime());
			}
		}
		cycleOperationToUpdate.setDescription(cycleOperation.getDescription() == null ? cycleOperationToUpdate.getDescription() : cycleOperation.getDescription());
		cycleOperationRepository.save(cycleOperationToUpdate);
	}

}
