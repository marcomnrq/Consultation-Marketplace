package com.marcomnrq.consultation.service;

import com.marcomnrq.consultation.domain.model.User;
import com.marcomnrq.consultation.domain.repository.UserRepository;
import com.marcomnrq.consultation.exception.CustomException;
import com.marcomnrq.consultation.resource.SaveUserResource;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.apache.http.entity.ContentType.*;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BucketService bucketService;

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

    public void uploadUserProfileImage(Long userId, MultipartFile file) {
        // 1. Check if image is not empty
        bucketService.isFileEmpty(file);
        // 2. If file is an image
        bucketService.isImage(file);

        // 3. The user exists in our database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found with specified id"));

        // 4. Grab some metadata from file if any
        Map<String, String> metadata = bucketService.extractMetadata(file);

        // 5. Store the image in s3 and update database (userProfileImageLink) with s3 image link
        String path = String.format("%s/%s", "bucketName", user.getId());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        System.out.println("Path: " + path);
        System.out.println("Filename: " + filename);
        try {
            bucketService.save(path, filename, Optional.of(metadata), file.getInputStream());
            user.setProfileImgLink(filename);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    public byte[] downloadUserProfileImage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found with specified id"));

        String path = String.format("%s/%s",
                "bucketName",
                user.getId());

        return user.getUserProfileImageLink().map(key -> bucketService.download(path, key)).orElse(new byte[0]);

    }
}
