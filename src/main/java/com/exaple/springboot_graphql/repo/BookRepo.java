package com.exaple.springboot_graphql.repo;

import com.exaple.springboot_graphql.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author comqiao
 * @create 2020-10-10 13:39
 */
public interface BookRepo extends JpaRepository<Book,Long> {
    List<Book> findByAuthorId(Long id);

    Book findBookById(Long id);
}
