package com.marcomnrq.consultation.resource;

import com.marcomnrq.consultation.domain.model.AuditModel;
import com.marcomnrq.consultation.domain.model.Currency;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Locale;

@Data
public class ListingResource extends AuditModel {

    @JsonIgnore
    private final PrettyTime p = new PrettyTime().setLocale(new Locale("ES"));

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

    public String created = p.format(getCreatedAt());
    public String updated = p.format(getUpdatedAt());

}
