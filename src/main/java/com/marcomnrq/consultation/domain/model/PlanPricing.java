package com.marcomnrq.consultation.domain.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "plan_pricings")
public class PlanPricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Currency currency;

    private Double price;
}
