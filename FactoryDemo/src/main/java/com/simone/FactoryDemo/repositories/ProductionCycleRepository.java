package com.simone.FactoryDemo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simone.FactoryDemo.models.ProductionCycle;

public interface ProductionCycleRepository extends JpaRepository<ProductionCycle,UUID>{

	ProductionCycle findByCode(String code);
	Long removeByCode(String code);

}
