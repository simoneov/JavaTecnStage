package com.simone.FactoryDemo.models;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.simone.FactoryDemo.enums.OrderState;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
	
	@Column(unique = true, nullable = false)
	private Long code;
	
	@Column(nullable = false)
	private Integer requestedQuantity;

	@Column(nullable =false)
	private Integer producedQuantity;

	@Column(nullable =false)
	private Integer scrappedQuantity;
	
	private ZonedDateTime deliveryDate;

	@Column(nullable =false)
	@Enumerated(EnumType.STRING)
	private OrderState state;

	private ZonedDateTime startDate;

	private ZonedDateTime endDate;

	@ManyToOne(targetEntity = Item.class)
	@JoinColumn(name = "item_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private Item item;
	
	@Column(name = "item_id")
	private UUID itemId;
	

}
