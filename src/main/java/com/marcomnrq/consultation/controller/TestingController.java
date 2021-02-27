package com.marcomnrq.consultation.controller;

import com.marcomnrq.consultation.domain.model.Plan;
import com.marcomnrq.consultation.domain.model.Role;
import com.marcomnrq.consultation.domain.model.User;
import com.marcomnrq.consultation.domain.repository.PlanRepository;
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

    private final PlanRepository planRepository;

    @GetMapping()
    public ResponseEntity<?> testingGetRequest(){
        throw new CustomException(1001, "Invalid message");
    }

    @PostMapping()
    public ResponseEntity<?> testingFunction() {
        // Users
        userRepository.deleteAll();

        // Plans
        planRepository.deleteAll();
        Plan plan = new Plan();
        plan.setName("PLAN_FREE");
        planRepository.save(plan);
        plan = new Plan();
        plan.setName("PLAN_PREMIUM");
        planRepository.save(plan);
        plan = new Plan();
        plan.setName("PLAN_BUSINESS");
        planRepository.save(plan);

        // Role
        roleRepository.deleteAll();
        Role role = new Role();
        role.setName("ROLE_USER");
        roleRepository.save(role);
        role = new Role();
        role.setName("ROLE_PROFESSIONAL");
        roleRepository.save(role);
        role = new Role();
        role.setName("ROLE_ADMINISTRATOR");
        roleRepository.save(role);
        return ResponseEntity.ok().build();
    }
}
