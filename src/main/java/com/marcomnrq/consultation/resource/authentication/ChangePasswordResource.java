package com.marcomnrq.consultation.resource.authentication;

import lombok.Data;

@Data
public class ChangePasswordResource {
    private String newPassword;
    private String oldPassword;
}
