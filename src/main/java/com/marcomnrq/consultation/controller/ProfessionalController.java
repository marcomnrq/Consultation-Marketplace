package com.marcomnrq.consultation.controller;

import com.marcomnrq.consultation.domain.model.Professional;
import com.marcomnrq.consultation.resource.ProfessionalResource;
import com.marcomnrq.consultation.resource.SaveProfessionalResource;
import com.marcomnrq.consultation.service.ProfessionalService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/professionals")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    private final ModelMapper modelMapper;

    @PostMapping
    public ProfessionalResource upgradeAccount(@RequestBody SaveProfessionalResource resource, Principal principal){
        String email = principal.getName();
        ProfessionalResource professionalResource = convertToResource(professionalService.createProfessionalProfile(email, resource));
        return professionalResource;
    }

    @PutMapping
    public ProfessionalResource editProfile(@RequestBody SaveProfessionalResource resource, Principal principal){
        String email = principal.getName();
        return convertToResource(professionalService.editProfessionalProfile(email, resource));
    }

    private Professional convertToEntity(SaveProfessionalResource resource) {
        return modelMapper.map(resource, Professional.class);
    }

    private ProfessionalResource convertToResource(Professional entity) {
        return modelMapper.map(entity, ProfessionalResource.class);
    }
}
