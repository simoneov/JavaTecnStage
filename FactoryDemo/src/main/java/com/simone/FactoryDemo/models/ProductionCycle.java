package com.simone.FactoryDemo.models;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Table(name = "productionCycles")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionCycle {

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
	
	@Column(unique=true, nullable = false)
	private String code;
	
	private String description;
	 
	@OneToMany(mappedBy = "productionCycle", cascade = CascadeType.ALL)
	private List<CycleOperation> cycleOperations;
}
