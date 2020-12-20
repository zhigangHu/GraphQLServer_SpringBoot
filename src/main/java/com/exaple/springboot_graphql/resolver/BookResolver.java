package com.exaple.springboot_graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import com.exaple.springboot_graphql.entity.Author;
import com.exaple.springboot_graphql.entity.Book;
import com.exaple.springboot_graphql.repo.AuthorRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author comqiao
 * @create 2020-10-12 12:10
 */
@Component
@AllArgsConstructor
public class BookResolver implements GraphQLResolver<Book> {
    private AuthorRepo authorRepo;

    public Author getAuthor(Book book) {
        return authorRepo.findAuthorById(book.getAuthorId());
    }

}
