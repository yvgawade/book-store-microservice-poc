package com.optimagrowth.book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.optimagrowth.book.model.Book;
import com.optimagrowth.book.service.BookService;

@RestController
@RequestMapping(value="v1/book")
public class BookController {
    @Autowired
    private BookService service;


    @RequestMapping(value="/{bookId}",method = RequestMethod.GET)
    public ResponseEntity<Book> getBook( @PathVariable("bookId") String bookId) {
        return ResponseEntity.ok(service.findById(bookId));
    }

    @RequestMapping(value="/{bookId}",method = RequestMethod.PUT)
    public void updateBook( @PathVariable("bookId") String id, @RequestBody Book book) {
        service.update(book);
    }

    @PostMapping
    public ResponseEntity<Book>  saveBook(@RequestBody Book book) {
    	return ResponseEntity.ok(service.create(book));
    }
   
    @RequestMapping(value="/{bookId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook( @PathVariable("bookId") String bookId) {
    	service.delete( bookId );

    }

}
