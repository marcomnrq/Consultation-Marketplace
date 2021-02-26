package com.marcomnrq.consultation.controller;

import com.marcomnrq.consultation.domain.model.Role;
import com.marcomnrq.consultation.domain.model.User;
import com.marcomnrq.consultation.domain.repository.RoleRepository;
import com.marcomnrq.consultation.domain.repository.UserRepository;
import com.marcomnrq.consultation.exception.CustomException;
import com.marcomnrq.consultation.resource.UserResource;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/tests")
public class TestingController {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @GetMapping()
    public ResponseEntity<?> testingGetRequest(){
        throw new CustomException(1001, "Invalid message");
    }

    @PostMapping()
    public ResponseEntity<?> testingFunction() {
        User user = userRepository.findByEmail("manriqueacham@gmail.com")
                .orElseThrow(()->new CustomException("error retrieving user"));
        List<Role> roleList = new ArrayList<>();
        roleList.add(roleRepository.findByName("ROLE_USER")
                .orElseThrow(()->new CustomException("error retrieving role")));
        user.setRoles(roleList);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
