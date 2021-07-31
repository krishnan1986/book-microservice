package com.store.book.repository;


import com.store.book.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book,Long> {
    Book findByTitle(String name);

    List<Book> findByAuthorId(String authorId);
}
