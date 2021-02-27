package com.marcomnrq.consultation.resource;

import com.marcomnrq.consultation.domain.model.AuditModel;
import lombok.Data;

@Data
public class ReviewResource extends AuditModel {

    private Long id;

    private Integer rating;

    private String content;

    private UserResource user;

    private String created, updated;

}
