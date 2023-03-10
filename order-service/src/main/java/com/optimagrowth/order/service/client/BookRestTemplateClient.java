package com.optimagrowth.order.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.optimagrowth.order.model.Book;
import com.optimagrowth.order.repository.BookRedisRepository;
import com.optimagrowth.order.utils.UserContext;

@Component
public class BookRestTemplateClient {
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	BookRedisRepository redisRepository;

	private static final Logger logger = LoggerFactory.getLogger(BookRestTemplateClient.class);

	public Book getBook(String bookId){
		logger.debug("In order Service.getBook: {}", UserContext.getCorrelationId());

        Book book = checkRedisCache(bookId);

        if (book != null){
            logger.debug("I have successfully retrieved an book {} from the redis cache: {}", bookId, book);
            return book;
        }

        logger.debug("Unable to locate book from the redis cache: {}.", bookId);
        
		ResponseEntity<Book> restExchange =
				restTemplate.exchange(
						"http://gateway:8072/book/v1/book/{bookId}",
						HttpMethod.GET,
						null, Book.class, bookId);
		
		/*Save the record from cache*/
        book = restExchange.getBody();
        if (book != null) {
            cacheBookObject(book);
        }

		return restExchange.getBody();
	}

	private Book checkRedisCache(String bookId) {
		try {
			return redisRepository.findById(bookId).orElse(null);
		}catch (Exception ex){
			logger.error("Error encountered while trying to retrieve book {} check Redis Cache.  Exception {}", bookId, ex);
			return null;
		}
	}
	
	private void cacheBookObject(Book book) {
        try {
        	redisRepository.save(book);
        }catch (Exception ex){
            logger.error("Unable to cache book {} in Redis. Exception {}", book.getId(), ex);
        }
    }
}
