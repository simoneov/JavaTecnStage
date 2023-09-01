package com.simone.FactoryDemo.models;

import java.util.UUID;

import com.simone.FactoryDemo.enums.MachineState;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "machines")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Machine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
	
	@Column(unique=true, nullable =false)
	private String code;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable =false)
	private MachineState state;
	
	private String description;
	


}
