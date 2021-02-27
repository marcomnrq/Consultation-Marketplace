package com.marcomnrq.consultation.resource;

import com.marcomnrq.consultation.domain.model.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SaveUserResource {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
}
