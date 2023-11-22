package com.demospringdata.dempCrudOperations.controller;

import com.demospringdata.dempCrudOperations.exception.PersonNotFoundException;
import com.demospringdata.dempCrudOperations.model.ErrorResponse;
import com.demospringdata.dempCrudOperations.model.Person;
import com.demospringdata.dempCrudOperations.model.PersonDataRepository;
import com.demospringdata.dempCrudOperations.service.PersonService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    @Autowired
    private PersonService personService;

    @Autowired
    private PersonDataRepository personDataRepository;

    @GetMapping
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

  /* @GetMapping("/{id}")
    public Person getPersonById(@PathVariable Long id) {
       logger.info("Request to get person by ID: {}", id);
        return personService.getPersonById(id);
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonByIdUsingCustomException(@PathVariable Long id) {
        try {
            Person person = personDataRepository.getByIdOrThrow(id);
             logger.info("Person found: {}", person);
            return new ResponseEntity<>(person, HttpStatus.OK);
        } catch (PersonNotFoundException e) {
        logger.warn("Person not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("byFirstName/{firstName}")
    public ResponseEntity<Object> getPersonByFirstName(@PathVariable String firstName) {
        try {
            List<Person> person = personService.findPersonsByFirstName(firstName);
            return new ResponseEntity<>(person, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            com.demospringdata.dempCrudOperations.model.ErrorResponse errorResponse = new com.demospringdata.dempCrudOperations.model.ErrorResponse("\"Person not found\"", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            com.demospringdata.dempCrudOperations.model.ErrorResponse errorResponse = new com.demospringdata.dempCrudOperations.model.ErrorResponse("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byLastName/{lastName}")
    public List<Person> getPersonByLastName(@PathVariable String lastName) {
        return personService.findPersonsByLastName(lastName);
    }

    @PostMapping
    public Person savePerson(@RequestBody Person person) {
        return personService.savePerson(person);
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        com.demospringdata.dempCrudOperations.model.ErrorResponse errorResponse = new com.demospringdata.dempCrudOperations.model.ErrorResponse("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
