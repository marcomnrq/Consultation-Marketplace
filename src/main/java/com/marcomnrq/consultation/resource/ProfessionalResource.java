package com.marcomnrq.consultation.resource;

import lombok.Data;

@Data
public class ProfessionalResource {
    private Long id;
    private UserResource user;
    private String shortName;
    private String profileName;
    private String description;
}
