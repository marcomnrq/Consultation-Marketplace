package com.marcomnrq.consultation.service;

import com.marcomnrq.consultation.domain.model.Role;
import com.marcomnrq.consultation.domain.repository.RoleRepository;
import com.marcomnrq.consultation.domain.repository.UserRepository;
import com.marcomnrq.consultation.exception.CustomException;
import com.marcomnrq.consultation.resource.SaveRoleResource;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public Role createRole(SaveRoleResource roleResource){
        Role role = new Role();
        role.setName(roleResource.getName());
        return roleRepository.save(role);
    }

    public Role editRole(String name, SaveRoleResource roleResource){
        Role role = roleRepository.findByName(name).orElseThrow(()-> new CustomException(1000, "Invalid name, not found"));
        role.setName(roleResource.getName());
        return roleRepository.save(role);
    }
}
