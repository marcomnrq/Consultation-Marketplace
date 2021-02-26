package com.marcomnrq.consultation.service;

import com.marcomnrq.consultation.domain.model.Professional;
import com.marcomnrq.consultation.domain.model.Role;
import com.marcomnrq.consultation.domain.model.User;
import com.marcomnrq.consultation.domain.repository.ProfessionalRepository;
import com.marcomnrq.consultation.domain.repository.RoleRepository;
import com.marcomnrq.consultation.domain.repository.UserRepository;
import com.marcomnrq.consultation.exception.CustomException;
import com.marcomnrq.consultation.resource.SaveProfessionalResource;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfessionalService {

    private final UserRepository userRepository;

    private final ProfessionalRepository professionalRepository;

    private final RoleRepository roleRepository;

    public Professional createProfessionalProfile(String email, SaveProfessionalResource professionalResource){
        // User upgrades to a professional account
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(1000, "Invalid username, no user match with email value"));
        if (professionalRepository.existsByShortName(professionalResource.getShortName())){
            throw new CustomException(1001, "Invalid short name, a profile already exists with that value");
        }
        if (user.getIsProfessional()){
            throw new CustomException(1002, "Invalid request, user is already a professional");
        }
        // Creating the professional profile
        Professional professional = new Professional();
        professional.setUser(user);
        professional.setShortName(professional.getShortName());
        professional.setProfileName(professional.getProfileName());
        professional.setDescription(professional.getDescription());
        /*
        Role professionalRole = roleRepository.findByName("ROLE_PROFESSIONAL")
                .orElseThrow(()->new CustomException(1003, "Something went wrong, professional role not found"));;
        if (!user.getRoles().contains(professionalRole)){
            user.getRoles().add(professionalRole);
        }

         */
        user.setIsProfessional(true);
        userRepository.save(user);
        return professionalRepository.save(professional);
    }

    public Professional editProfessionalProfile(String email, SaveProfessionalResource professionalResource){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(1000, "Invalid username, no user match with email value"));
        Professional professional = professionalRepository.findByUser(user)
                .orElseThrow(()->new CustomException(1003, "Invalid request, user is not a professional"));
        if(!professional.getShortName().equals(professionalResource.getShortName())) {
            if (professionalRepository.existsByShortName(professionalResource.getShortName())) {
                throw new CustomException(1001, "Invalid short name, a profile already exists with that value");
            } else{
                professional.setShortName(professionalResource.getShortName());
            }
        }
        professional.setProfileName(professionalResource.getProfileName());
        professional.setDescription(professionalResource.getDescription());
        return professionalRepository.save(professional);
    }

    public Professional getProfessionalByShortName(String shortName){
        return professionalRepository.findByShortName(shortName).orElseThrow(()->new CustomException(1001, "Invalid short name, no profile found"));
    }

    public Page<Professional> getAllProfessionals(Pageable pageable){
        return professionalRepository.findAll(pageable);
    }

}
