package com.marcomnrq.consultation.domain.model;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
