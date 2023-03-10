package com.optimagrowth.order.events.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BookChangeModel {
	private String type;
    private String action;
    private String bookId;
    private String correlationId;

    public BookChangeModel(){
        super();
    }

    public  BookChangeModel(String type, String action, String bookId, String correlationId) {
        super();
        this.type   = type;
        this.action = action;
        this.bookId = bookId;
        this.correlationId = correlationId;
    }
}
