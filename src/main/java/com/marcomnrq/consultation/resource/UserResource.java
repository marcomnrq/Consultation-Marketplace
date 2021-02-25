package com.marcomnrq.consultation.resource;

import com.marcomnrq.consultation.domain.model.Gender;
import lombok.Data;

import java.util.List;

@Data
public class UserResource {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;
    private List<RoleResource> roles;
    private Boolean isProfessional;
}
