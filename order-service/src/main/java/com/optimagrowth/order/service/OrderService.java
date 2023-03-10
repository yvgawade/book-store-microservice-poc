package com.optimagrowth.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.optimagrowth.order.config.ServiceConfig;
import com.optimagrowth.order.model.Order;
import com.optimagrowth.order.model.Book;
import com.optimagrowth.order.repository.OrderRepository;
import com.optimagrowth.order.service.client.BookDiscoveryClient;
import com.optimagrowth.order.service.client.BookFeignClient;
import com.optimagrowth.order.service.client.BookRestTemplateClient;
import com.optimagrowth.order.utils.UserContextHolder;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.bulkhead.annotation.Bulkhead.Type;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class OrderService {

	@Autowired
	MessageSource messages;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	ServiceConfig config;

	@Autowired
	BookFeignClient bookFeignClient;

	@Autowired
	BookRestTemplateClient bookRestClient;

	@Autowired
	BookDiscoveryClient bookDiscoveryClient;
	
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	public Order getOrder(String orderId, String bookId, String clientType){
		Order order = orderRepository.findByBookIdAndOrderId(bookId, orderId);
		if (null == order) {
			throw new IllegalArgumentException(String.format(messages.getMessage("order.search.error.message", null, null),orderId, bookId));	
		}

		Book book = retrieveBookInfo(bookId, clientType);


		return order;
	}

	private Book retrieveBookInfo(String bookId, String clientType) {
		Book book = null;

		switch (clientType) {
		case "feign":
			System.out.println("I am using the feign client");
			book = bookFeignClient.getBook(bookId);
			break;
		case "rest":
			System.out.println("I am using the rest client");
			book = bookRestClient.getBook(bookId);
			break;
		case "discovery":
			System.out.println("I am using the discovery client");
			book = bookDiscoveryClient.getBook(bookId);
			break;
		default:
			book = bookRestClient.getBook(bookId);
			break;
		}

		return book;
	}

	public Order createOrder(Order order){
		order.setOrderId(UUID.randomUUID().toString());
		orderRepository.save(order);

		return order;
	}

	public Order updateOrder(Order order){
		orderRepository.save(order);

		return order;
	}

	public String deleteOrder(String orderId){
		String responseMessage = null;
		Order order = new Order();
		order.setOrderId(orderId);
		orderRepository.delete(order);
		responseMessage = String.format(messages.getMessage("order.delete.message", null, null),orderId);
		return responseMessage;

	}

	@CircuitBreaker(name = "orderService", fallbackMethod = "buildFallbackOrderList")
	@RateLimiter(name = "orderService", fallbackMethod = "buildFallbackOrderList")
	@Retry(name = "retryOrderService", fallbackMethod = "buildFallbackOrderList")
	@Bulkhead(name = "bulkheadOrderService", type= Type.THREADPOOL, fallbackMethod = "buildFallbackOrderList")
	public List<Order> getOrdersByBook(String bookId) throws TimeoutException {
		 logger.debug("getOrdersByBook Correlation id: {}",
			UserContextHolder.getContext().getCorrelationId());
		randomlyRunLong();
		return orderRepository.findByBookId(bookId);
	}

	@SuppressWarnings("unused")
	private List<Order> buildFallbackOrderList(String bookId, Throwable t){
		List<Order> fallbackList = new ArrayList<>();
		Order order = new Order();
		order.setOrderId("0000000-00-00000");
		order.setBookId(bookId);
		order.setCustomerName("XYZ");
		fallbackList.add(order);
		return fallbackList;
	}

	private void randomlyRunLong(){
		Random rand = new Random();
		int randomNum = rand.nextInt((3 - 1) + 1) + 1;
		if (randomNum==3) sleep();
	}
	private void sleep(){
		try {
			Thread.sleep(11000);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
	}
}
