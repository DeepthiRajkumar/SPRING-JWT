package com.demospringdata.dempCrudOperations.model;

import com.demospringdata.dempCrudOperations.exception.PersonNotFoundException;
import com.demospringdata.dempCrudOperations.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonDataRepository extends JpaRepository<Person, Long> {

    // Custom query using JPQL (Java Persistence Query Language) to find persons by last name
    @Query("SELECT p FROM Person p WHERE p.lastName = ?1")
    List<Person> findPersonsByLastName(String lastName);
    @Query("SELECT p FROM Person p WHERE p.firstName = ?1")
    List<Person> findPersonsByFirstName(String firstName);


    default Person getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new PersonNotFoundException("Person not found with ID: " + id));
    }
}

