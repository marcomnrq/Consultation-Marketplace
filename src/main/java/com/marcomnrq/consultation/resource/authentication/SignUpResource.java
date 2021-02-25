package com.marcomnrq.consultation.resource.authentication;

import com.marcomnrq.consultation.domain.model.Gender;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class SignUpResource {
    private String email;

    @Size(max = 20, min = 6)
    private String password;

    private String firstName;

    private String lastName;
}
