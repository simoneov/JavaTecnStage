package com.simone.FactoryDemo.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.simone.FactoryDemo.models.Operation;
@Repository
public interface OperationRepository extends JpaRepository<Operation, UUID>{

	@Query(value = "SELECT "
			+ "o.id,o.code,o.description,o.start_date,o.state,o.produced_quantity,o.durata_min,"
			+ "o.end_date,o.machine_id,o.order_id,o.scrapped_quantity FROM operations o "
			+ "JOIN orders ord ON (o.order_id = ord.id) WHERE o.code = ?1 AND ord.code = ?2", nativeQuery = true)
	Operation findByCodeAndOrderCode(Integer code, Long orderCode);

	@Modifying
	@Query(value = "DELETE FROM operations o USING orders ord WHERE "
			+ "(o.order_id = ord.id) AND o.code = ?1 AND ord.code = ?2", nativeQuery = true)
	void removeByCodeAndOrderCode(Integer code, Long orderCode);
	
	//TO TRY
	@Query(value = "SELECT SUM (produced_quantity) AS total FROM operations o WHERE o.order_id = ?1", nativeQuery = true)
	Integer sumManufacturedQuantityPerOrder(Long orderId);
	
	//TO TRY
	@Query(value = "SELECT SUM (scrapped_quantity) AS total FROM operations o WHERE o.order_id = ?1", nativeQuery = true)
	Integer sumDiscardedQuantityPerOrder(Long orderId);
	
	//TO TRY
	@Query(value = "SELECT MAX (code) AS operationCode FROM operations o WHERE o.order_id = ?1", nativeQuery = true)
	Integer lastOrderOperation(UUID orderId);
	
	@Query(value = "SELECT o.id,o.code,o.description,o.durata_min,o.end_date,o.machine_id,o.order_id,o.produced_quantity,o.scrapped_quantity,o.start_date,"
			+ "o.state FROM operations o WHERE o.order_id = ?1", nativeQuery = true)
	List<Operation> operationsPerOrder(UUID orderId);
}
