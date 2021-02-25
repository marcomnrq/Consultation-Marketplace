package com.marcomnrq.consultation.resource;

import com.marcomnrq.consultation.domain.model.Currency;
import lombok.Data;

@Data
public class SaveListingResource {

    private String title;

    private String description;

    private Currency currency;

    private Double price;

    private Integer categoryId;

    private Boolean visible;
}
