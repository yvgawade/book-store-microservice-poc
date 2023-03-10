package com.optimagrowth.order.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.optimagrowth.order.model.Book;

@Repository
public interface BookRedisRepository extends CrudRepository<Book,String>  {
}
