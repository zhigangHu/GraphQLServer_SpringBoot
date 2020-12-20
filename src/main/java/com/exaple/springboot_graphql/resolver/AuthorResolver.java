package com.exaple.springboot_graphql.resolver;

import com.exaple.springboot_graphql.entity.Author;
import com.exaple.springboot_graphql.entity.Book;
import graphql.kickstart.tools.GraphQLResolver;

import com.exaple.springboot_graphql.repo.BookRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author comqiao
 * @create 2020-10-12 12:03
 */
@Component
@AllArgsConstructor
public class AuthorResolver implements GraphQLResolver<Author> {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private BookRepo bookRepo;

    public String getCreatedTime(Author author) {
        return sdf.format(author.getCreatedTime());
    }

    public List<Book> getBooks(Author author) {
        return bookRepo.findByAuthorId(author.getId());
    }


}
