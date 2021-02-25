package com.marcomnrq.consultation.resource.authentication;

import lombok.Data;

@Data
public class ResetPasswordResource {

    private String token;

    private String oldPassword;

    private String newPassword;

}
