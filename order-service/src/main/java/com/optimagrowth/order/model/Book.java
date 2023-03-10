package com.optimagrowth.order.model;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@RedisHash("book")
public class Book extends RepresentationModel<Book> {

	@Id
	String id;
    String name;
    
}
