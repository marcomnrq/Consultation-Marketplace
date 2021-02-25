package com.marcomnrq.consultation.resource;

import com.marcomnrq.consultation.domain.model.Currency;
import lombok.Data;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Locale;

@Data
public class ListingResource {
    private Long id;
    private UserResource user;
    private Boolean visible;
    private String title;
    private String description;
    private Boolean featured;
    private Double rating;
    private Integer ratingsCount;
    private Currency currency;
    private Double price;
    private CategoryResource category;
}
