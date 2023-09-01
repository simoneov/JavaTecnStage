package com.simone.FactoryDemo.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.simone.FactoryDemo.models.CycleOperation;

public interface CycleOperationRepository extends JpaRepository<CycleOperation,UUID>{

	
	@Query(value = "SELECT c.id,c.description,c.invariant_duration,c.machine_id,c.operation_code,c.production_cycle_id"
			+ ",c.type,c.working_quantity,c.working_time FROM cycle_operations c JOIN production_cycles pc "
			+ "ON (c.production_cycle_id = pc.id) "
			+ "WHERE pc.id = ?1", nativeQuery = true)
	List<CycleOperation> findAllCycleOperationsByCycle(UUID code);
	
	//TO TEST
	@Query(value = "SELECT c.id,c.description,c.invariant_duration,c.machine_id,c.operation_code,c.production_cycle_id"
			+ ",c.type,c.working_quantity,c.working_time FROM cycle_operations c JOIN production_cycles pc "
			+ "ON (c.production_cycle_id = pc.id) "
			+ "WHERE pc.code = ?1 AND c.operation_code = ?2", nativeQuery = true)
	CycleOperation findByProductionCycleAndOperationCode(String productionCycleCode, Integer operationCode);
	
	//TO TEST
	@Modifying
	@Query(value = "DELETE FROM cycle_operations c USING production_cycles pc WHERE (c.production_cycle_id = pc.id) AND pc.code = ?1 AND c.operation_code = ?2", nativeQuery = true)
	void removeByProductionCycleAndOperationCode(String productionCycleCode, Integer operationCode);
}
