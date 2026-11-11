package com.example.application.services;

import com.example.application.data.model.Company;
import com.example.application.data.model.Contact;
import com.example.application.data.model.Status;
import com.example.application.data.repository.CompanyRepository;
import com.example.application.data.repository.ContactRepository;
import com.example.application.data.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service // ① Inversion of Control (IoC) container manages the lifecycle of the service class and its dependencies.
public class CrmService {
    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    private final StatusRepository statusRepository;

    // ② Use Spring constructor injection to autowire the database repositories.
    public CrmService(ContactRepository contactRepository,
                      CompanyRepository companyRepository,
                      StatusRepository statusRepository) {

        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
        this.statusRepository = statusRepository;
    }


    public List<Contact> findAllContacts(String stringFilter) {

        // ③ Check if there’s an active filter: return either all contacts, or use the repository to filter based
        //    on the string.
        if (stringFilter == null || stringFilter.isEmpty())
            return contactRepository.findAll();
        else
            return contactRepository.search(stringFilter);
    }



    public long countContacts() {
        return contactRepository.count();
    }


    public void deleteContact(Contact contact) {contactRepository.delete(contact);
    }


    public void saveContact(Contact contact) {

        // ④ Service classes often include validation and other business rules before persisting data. You
        //    check here that you aren’t trying to save a null object.
        if (contact == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        contactRepository.save(contact);
        System.out.println("Contact saved: " + contact.getFirstName() + " " + contact.getLastName());

    }


    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }


    public List<Status> findAllStatuses(){
        return statusRepository.findAll();
    }
}