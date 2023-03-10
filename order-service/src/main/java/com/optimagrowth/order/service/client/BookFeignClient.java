package com.optimagrowth.order.service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.optimagrowth.order.model.Book;

@FeignClient("book-service")
public interface BookFeignClient {
    @RequestMapping(
            method= RequestMethod.GET,
            value="/v1/book/{bookId}",
            consumes="application/json")
    Book getBook(@PathVariable("bookId") String bookId);
}
