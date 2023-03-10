package com.optimagrowth.order.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.optimagrowth.order.model.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order,String>  {
    public List<Order> findByBookId(String bookId);
    public Order findByBookIdAndOrderId(String bookId,String orderId);
}
