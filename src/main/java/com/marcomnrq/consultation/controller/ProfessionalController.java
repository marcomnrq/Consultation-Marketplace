package com.marcomnrq.consultation.controller;

import com.marcomnrq.consultation.domain.model.Listing;
import com.marcomnrq.consultation.domain.model.Professional;
import com.marcomnrq.consultation.resource.ListingResource;
import com.marcomnrq.consultation.resource.ProfessionalResource;
import com.marcomnrq.consultation.resource.SaveProfessionalResource;
import com.marcomnrq.consultation.service.ProfessionalService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("{id}")
    public ProfessionalResource getProfessionalById(@PathVariable(name = "id") Long id){
        return convertToResource(professionalService.getProfessionalById(id));
    }

    @GetMapping()
    public Page<ProfessionalResource> getAllProfessionals(Pageable pageable){
        Page<Professional> professionals = professionalService.getAllProfessionals(pageable);
        List<ProfessionalResource> resources = professionals.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    private Professional convertToEntity(SaveProfessionalResource resource) {
        return modelMapper.map(resource, Professional.class);
    }

    private ProfessionalResource convertToResource(Professional entity) {
        return modelMapper.map(entity, ProfessionalResource.class);
    }
}
