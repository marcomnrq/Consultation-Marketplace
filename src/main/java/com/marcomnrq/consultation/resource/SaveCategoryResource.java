package com.marcomnrq.consultation.resource;

import lombok.Data;

@Data
public class SaveCategoryResource {

    private Boolean active;

    private String name;

    private Integer parentId;

}
