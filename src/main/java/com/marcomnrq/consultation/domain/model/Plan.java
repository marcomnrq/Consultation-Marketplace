package com.marcomnrq.consultation.domain.model;

import java.util.List;

public class Plan {
    private Integer id;
    private String name;
    private Double price;
    private List<PlanPricing> pricingList;
}
