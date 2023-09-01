package com.simone.FactoryDemo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.simone.FactoryDemo.models.Order;
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>{

	Order findByCode(Long code);
	Long removeByCode(Long code);
	
	@Query(value = "SELECT MAX (code) AS orderCode FROM orders", nativeQuery = true)
	Long lastOrder();

}
