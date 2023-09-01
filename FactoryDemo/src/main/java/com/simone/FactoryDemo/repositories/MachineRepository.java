package com.simone.FactoryDemo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simone.FactoryDemo.models.Machine;

@Repository
public interface MachineRepository extends JpaRepository<Machine, UUID>{

	Machine findByCode(String code);
	Long removeByCode(String code);


}
