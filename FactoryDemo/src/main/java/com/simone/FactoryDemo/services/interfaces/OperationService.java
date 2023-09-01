package com.simone.FactoryDemo.services.interfaces;

import java.util.List;
import org.springframework.stereotype.Service;
import com.simone.FactoryDemo.dtos.insertdtos.InsertOperationDto;
import com.simone.FactoryDemo.dtos.updatedtos.StartWorkDto;
import com.simone.FactoryDemo.dtos.updatedtos.SuspendWorkDto;
import com.simone.FactoryDemo.dtos.updatedtos.ProduceQuantityDto;
import com.simone.FactoryDemo.exceptions.InvalidOperationException;
import com.simone.FactoryDemo.models.Operation;
import com.simone.FactoryDemo.viewmodels.OperationViewModel;

@Service
public interface OperationService {

	public Operation findByCodeAndOrderCode(Integer code, Long orderCode);
	public List<OperationViewModel> getAll(); 
	public void startWork(StartWorkDto operation) throws InvalidOperationException;
	public void suspendWork(SuspendWorkDto operation) throws InvalidOperationException;
	public void produceQuantity(ProduceQuantityDto operation) throws InvalidOperationException;
	public void endOperation(Integer operationCode, Long orderCode) throws InvalidOperationException;
	public void create(InsertOperationDto operation) throws InvalidOperationException;
	public void delete(Integer code, Long orderCode);
	
}
