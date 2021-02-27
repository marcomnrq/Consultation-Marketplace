package com.marcomnrq.consultation.service;

import com.marcomnrq.consultation.domain.model.User;
import com.marcomnrq.consultation.domain.repository.UserRepository;
import com.marcomnrq.consultation.exception.CustomException;
import com.marcomnrq.consultation.resource.SaveUserResource;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User editDetails(String email, SaveUserResource resource){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found with username"));
        user.setFirstName(resource.getFirstName());
        user.setLastName(resource.getLastName());
        user.setGender(resource.getGender());
        user.setDateOfBirth(resource.getDateOfBirth());
        return userRepository.save(user);
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found with username"));
    }

    public Page<User> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    public ResponseEntity<?> deleteAccount(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found with username"));
        // TODO: improve account deleting
        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }
}
