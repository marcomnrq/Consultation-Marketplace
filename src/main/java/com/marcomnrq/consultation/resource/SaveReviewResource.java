package com.marcomnrq.consultation.resource;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class SaveReviewResource {
    @Size(min = 1, max = 5)
    private Integer rating;

    @Size(min = 3, max = 300)
    private String content;
}
