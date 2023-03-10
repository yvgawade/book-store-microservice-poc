package com.optimagrowth.book.events.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.optimagrowth.book.events.model.BookChangeModel;
import com.optimagrowth.book.utils.UserContext;

@Component
public class SimpleSourceBean {
    private Source source;

    private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);

    @Autowired
    public SimpleSourceBean(Source source){
        this.source = source;
    }

    public void publishBookChange(String action, String bookId){
       logger.debug("Sending Kafka message {} for Book Id: {}", action, bookId);
        BookChangeModel change =  new BookChangeModel(
                BookChangeModel.class.getTypeName(),
                action,
                bookId,
                UserContext.getCorrelationId());

        source.output().send(MessageBuilder.withPayload(change).build());
    }
}
