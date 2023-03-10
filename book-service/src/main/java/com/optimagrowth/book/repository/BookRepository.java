package com.optimagrowth.book.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.optimagrowth.book.model.Book;

@Repository
public interface BookRepository extends CrudRepository<Book,String>  {
    public Optional<Book> findById(String bookId);
}
