package com.marcomnrq.consultation.service;

import com.marcomnrq.consultation.domain.model.Contact;
import com.marcomnrq.consultation.domain.repository.ContactRepository;
import com.marcomnrq.consultation.domain.repository.ListingRepository;
import com.marcomnrq.consultation.exception.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    private final ListingRepository listingRepository;

    public Contact createContact(Long listingId, Contact contact){
        contact.setListing(listingRepository.findById(listingId).orElseThrow(()-> new CustomException("Invalid listing")));
        return contactRepository.save(contact);
    }

    public Contact updateContact(Long contactId, Contact resource){
        Contact contact = contactRepository.findById(contactId).orElseThrow(()-> new CustomException("Invalid id"));
        contact.setContactType(resource.getContactType());
        contact.setValue(resource.getValue());
        return contactRepository.save(contact);
    }

    public Contact getContactById(Long id){
        return contactRepository.findById(id).orElseThrow(()-> new CustomException("Invalid id"));
    }

    public Page<Contact> getAllContacts(Pageable pageable){
        return contactRepository.findAll(pageable);
    }

    public Page<Contact> getAllContactsByListingId(Long listingId, Pageable pageable){
        return contactRepository.findByListingId(listingId, pageable);
    }

    public ResponseEntity<?> deleteContact(Long contactId){
        Contact contact = contactRepository.findById(contactId).orElseThrow(()-> new CustomException("Invalid id"));
        contactRepository.delete(contact);
        return ResponseEntity.ok().build();
    }

}
