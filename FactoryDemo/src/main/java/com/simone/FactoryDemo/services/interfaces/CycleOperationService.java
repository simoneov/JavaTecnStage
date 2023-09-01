package com.simone.FactoryDemo.services.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

import com.simone.FactoryDemo.dtos.insertdtos.InsertCycleOperationDto;
import com.simone.FactoryDemo.dtos.updatedtos.UpdateCycleOperationDto;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.CycleOperation;
import com.simone.FactoryDemo.viewmodels.CycleOperationViewModel;

@Service
public interface CycleOperationService {

	public void create(InsertCycleOperationDto cycleOperation) throws InvalidOperationException;
	public void delete(String productionCycleCode, Integer operationCode);
	public CycleOperation findByProductionCycleAndOperationCode(String productionCycleCode, Integer operationCode);
	public List<CycleOperationViewModel> getAll(); 
	public void update(UpdateCycleOperationDto cycleOperation) throws InvalidOperationException;
}
