package com.marcomnrq.consultation.domain.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "plans")
public class Plan extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

}
