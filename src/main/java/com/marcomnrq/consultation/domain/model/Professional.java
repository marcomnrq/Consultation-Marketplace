package com.marcomnrq.consultation.domain.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "professionals")
public class Professional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user", updatable = false, nullable = false)
    private User user;

    private String shortName;

    private String profileName;

    private String description;
}
