package com.simone.FactoryDemo.models;

import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simone.FactoryDemo.enums.DurationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "cycleOperations", uniqueConstraints={
	    @UniqueConstraint(columnNames = {"production_cycle_id", "operationCode"})
	})
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CycleOperation {

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
		
	private String description;
	
	@Column(nullable = false)
	private Integer operationCode;
	
	@Column(nullable =false)
	@Enumerated(EnumType.STRING)
	private DurationType type; 

	private Integer workingQuantity;
	
	private Integer workingTime;
	
	private Integer invariantDuration;
	
	@ManyToOne(targetEntity = Machine.class)
	@JsonIgnore
    @JoinColumn(name = "machine_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
	private Machine machine;
	
	@Column(name = "machine_id")
	private UUID machineId;
	
	@ManyToOne(targetEntity = ProductionCycle.class)
	@JsonIgnore
    @JoinColumn(name = "production_cycle_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private ProductionCycle productionCycle;
	
	@Column(name = "production_cycle_id")
	private UUID productionCycleId;

}
