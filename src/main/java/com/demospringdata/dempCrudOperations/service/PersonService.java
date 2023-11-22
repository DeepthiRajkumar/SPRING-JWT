package com.demospringdata.dempCrudOperations.service;

import com.demospringdata.dempCrudOperations.model.PersonDataRepository;
import com.demospringdata.dempCrudOperations.model.Person;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    @Autowired
    private PersonDataRepository personRepository;

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person getPersonById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }

    public List<Person> findPersonsByLastName(String lastName) {
        return personRepository.findPersonsByLastName(lastName);
    }

    public List<Person> findPersonsByFirstName(String firstName) {
        List<Person> personsByLastName = personRepository.findPersonsByFirstName(firstName);
        if(personsByLastName.isEmpty()){
            throw new EntityNotFoundException();
        }
        return personsByLastName;
    }
}
