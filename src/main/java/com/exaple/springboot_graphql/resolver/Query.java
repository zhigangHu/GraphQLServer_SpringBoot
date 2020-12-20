package com.exaple.springboot_graphql.resolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import com.exaple.springboot_graphql.entity.Author;
import com.exaple.springboot_graphql.entity.Book;
import com.exaple.springboot_graphql.repo.AuthorRepo;
import com.exaple.springboot_graphql.repo.BookRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author comqiao
 * @create 2020-10-17 12:40
 */
@Component
@AllArgsConstructor
public class Query implements GraphQLQueryResolver {

    private AuthorRepo authorRepo;

    private BookRepo bookRepo;

    public Author findAuthorById(Long id) {
        return authorRepo.findAuthorById(id);
    }

    public List<Author> findAllAuthors() {
        return authorRepo.findAll();
    }

    public Long countAuthors() {
        return authorRepo.count();
    }

    public List<Book> findAllBooks() {
        return bookRepo.findAll();
    }

    public Long countBooks() {
        return bookRepo.count();
    }
}
