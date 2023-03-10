package com.optimagrowth.order.events.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.optimagrowth.order.events.CustomChannels;
import com.optimagrowth.order.events.model.BookChangeModel;

@EnableBinding(CustomChannels.class)
public class BookChangeHandler {

    private static final Logger logger = LoggerFactory.getLogger(BookChangeHandler.class);

    @StreamListener("inboundOrgChanges")
    public void loggerSink(BookChangeModel book) {
    	
        logger.debug("Received a message of type " + book.getType());
        
        switch(book.getAction()){
            case "GET":
                logger.debug("Received a GET event from the book service for book id {}", book.getBookId());
                break;
            case "SAVE":
                logger.debug("Received a SAVE event from the book service for book id {}", book.getBookId());
                break;
            case "UPDATE":
                logger.debug("Received a UPDATE event from the book service for book id {}", book.getBookId());
                break;
            case "DELETE":
                logger.debug("Received a DELETE event from the book service for book id {}", book.getBookId());
                break;
            default:
                logger.error("Received an UNKNOWN event from the book service of type {}", book.getType());
                break;
        }
    }


}
