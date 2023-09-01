package com.simone.FactoryDemo.models;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simone.FactoryDemo.enums.OperationState;

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
@Entity
@Table(name = "operations", uniqueConstraints={
	    @UniqueConstraint(columnNames = {"order_id", "code"})
	})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Operation {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
	
	@Column(nullable = false)
    private Integer code;
	
	private String description;
	
	@Column(nullable =false)
	@Enumerated(EnumType.STRING)
	private OperationState state; 

	@Column(nullable =false)
	private Integer producedQuantity;
	
	@Column(nullable =false)
	private Integer scrappedQuantity;
	
	private Date startDate;
	
	private Integer durataMin;
	
	private Date endDate;
	
	@ManyToOne(targetEntity = Order.class)
	@JsonIgnore
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;
	
	@Column(name = "order_id")
	private UUID orderId;
	
	@ManyToOne(targetEntity = Machine.class)
	@JsonIgnore
    @JoinColumn(name = "machine_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Machine machine;
	
	@Column(name = "machine_id")
	private UUID machineId;
}
