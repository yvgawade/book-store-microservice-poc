package com.optimagrowth.order.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.optimagrowth.order.model.Order;
import com.optimagrowth.order.service.OrderService;
import com.optimagrowth.order.utils.UserContextHolder;

@RestController
@RequestMapping(value="v1/book/{bookId}/order")
public class
OrderController {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@RequestMapping(value="/{orderId}",method = RequestMethod.GET)
	public ResponseEntity<Order> getOrder( @PathVariable("bookId") String bookId,
			@PathVariable("orderId") String orderId) {

		Order order = orderService.getOrder(orderId, bookId, "");
		order.add( 
				linkTo(methodOn(OrderController.class).getOrder(bookId, order.getOrderId())).withSelfRel(),
				linkTo(methodOn(OrderController.class).createOrder(order)).withRel("createOrder"),
				linkTo(methodOn(OrderController.class).updateOrder(order)).withRel("updateOrder"),
				linkTo(methodOn(OrderController.class).deleteOrder(order.getOrderId())).withRel("deleteOrder")
				);

		return ResponseEntity.ok(order);
	}

	@RequestMapping(value="/{orderId}/{clientType}",method = RequestMethod.GET)
	public Order getOrdersWithClient( @PathVariable("bookId") String bookId,
			@PathVariable("orderId") String orderId,
			@PathVariable("clientType") String clientType) {

		return orderService.getOrder(orderId, bookId, clientType);
	}

	@PutMapping
	public ResponseEntity<Order> updateOrder(@RequestBody Order request) {
		return ResponseEntity.ok(orderService.updateOrder(request));
	}

	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody Order request) {
		return ResponseEntity.ok(orderService.createOrder(request));
	}

	@DeleteMapping(value="/{orderId}")
	public ResponseEntity<String> deleteOrder(@PathVariable("orderId") String orderId) {
		return ResponseEntity.ok(orderService.deleteOrder(orderId));
	}

	@RequestMapping(value="/",method = RequestMethod.GET)
	public List<Order> getOrders( @PathVariable("bookId") String bookId) throws TimeoutException {
		logger.debug("OrderServiceController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
		return orderService.getOrdersByBook(bookId);
	}

}
