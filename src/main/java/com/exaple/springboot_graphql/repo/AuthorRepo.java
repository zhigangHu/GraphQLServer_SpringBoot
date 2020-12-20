package com.exaple.springboot_graphql.repo;

import com.exaple.springboot_graphql.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author comqiao
 * @create 10-10-2020 13:35
 */
public interface AuthorRepo extends JpaRepository<Author,Long> {

    Author findAuthorById(Long id);
}
