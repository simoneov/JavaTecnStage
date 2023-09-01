package com.simone.FactoryDemo.models;

import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "items")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("orders")
public class Item {

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
	
	@Column(unique=true, nullable = false)
	private String code;
	
	@Column(nullable = false)
	private Integer quantity;
	
	private String description;
	
	@ManyToOne(targetEntity = ProductionCycle.class)
	@JsonIgnore
    @JoinColumn(name = "production_cycle_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private ProductionCycle productionCycle;
	
	@Column(name = "production_cycle_id")
	private UUID productionCycleId;
	
}
