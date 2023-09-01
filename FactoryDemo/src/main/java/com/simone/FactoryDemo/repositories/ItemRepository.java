package com.simone.FactoryDemo.repositories;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simone.FactoryDemo.models.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID>{

	Item findByCode(String code);
	Long removeByCode(String code);
	
}
