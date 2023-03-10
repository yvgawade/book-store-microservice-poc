package com.optimagrowth.order.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@Entity
@Table(name="orders")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order extends RepresentationModel<Order> {

	@Id
	@Column(name = "order_id", nullable = false)
	private String orderId;
	private String description;
	@Column(name = "book_id", nullable = false)
	private String bookId;
	@Column(name = "customer_name", nullable = false)
	private String customerName;

}