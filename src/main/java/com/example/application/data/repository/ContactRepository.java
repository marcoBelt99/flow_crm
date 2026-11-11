package com.example.application.data.repository;

import com.example.application.data.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    /**
     * This example uses the @Query annotation to define a custom query
     * In this
     * case, it checks if the string matches the first or the last name, and ignores the case. The query
     * uses Java Persistence Query Language (JPQL) which is an SQL-like language for querying JPA-managed databases.
     * You don’t need to implement the method. Spring Data provides the implementation based on
     * the query.
     * @param searchTerm
     * @return
     */


    @Query("select c from Contact c " +
            "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<Contact> search(@Param("searchTerm") String searchTerm);
}
