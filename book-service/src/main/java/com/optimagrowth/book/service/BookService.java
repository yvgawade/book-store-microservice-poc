package com.optimagrowth.book.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimagrowth.book.events.source.SimpleSourceBean;
import com.optimagrowth.book.model.Book;
import com.optimagrowth.book.repository.BookRepository;

@Service
public class BookService {
	
	private static final Logger logger = LoggerFactory.getLogger(BookService.class);
	
    @Autowired
    private BookRepository repository;
    
    @Autowired
    SimpleSourceBean simpleSourceBean;

    public Book findById(String bookId) {
    	Optional<Book> opt = repository.findById(bookId);
    	simpleSourceBean.publishBookChange("GET", bookId);
        return (opt.isPresent()) ? opt.get() : null;
    }	

    public Book create(Book book){
    	book.setId( UUID.randomUUID().toString());
        book = repository.save(book);
        simpleSourceBean.publishBookChange("SAVE", book.getId());
        return book;

    }

    public void update(Book book){
    	repository.save(book);
        simpleSourceBean.publishBookChange("UPDATE", book.getId());
    }

    public void delete(String bookId){
    	repository.deleteById(bookId);
    	simpleSourceBean.publishBookChange("DELETE", bookId);
    }
    
    @SuppressWarnings("unused")
	private void sleep(){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
	}
}