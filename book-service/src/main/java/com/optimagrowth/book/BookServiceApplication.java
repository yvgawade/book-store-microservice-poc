package com.optimagrowth.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@SpringBootApplication
@RefreshScope
@EnableBinding(Source.class)
public class BookServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BookServiceApplication.class, args);
	}

}
