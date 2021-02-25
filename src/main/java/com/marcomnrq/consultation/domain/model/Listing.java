package com.marcomnrq.consultation.domain.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "listings")
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Boolean featured;

    private Double rating;

    private Currency currency;

    private Double price;

    private List<Contact> contactList;
}
