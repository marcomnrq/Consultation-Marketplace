package com.marcomnrq.consultation.resource.authentication;

import lombok.Data;

@Data
public class TotpSignInResource {
    private String email;
    private String password;
    private String totp;
}
