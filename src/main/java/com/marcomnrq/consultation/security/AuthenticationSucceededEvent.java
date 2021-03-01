package com.marcomnrq.consultation.security;

import com.marcomnrq.consultation.service.SentinelService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationSucceededEvent implements ApplicationListener<AuthenticationSuccessEvent> {

    private final SentinelService sentinelService;

    // This class is an event listener for failed authentication

    public void onApplicationEvent(AuthenticationSuccessEvent e) {
        WebAuthenticationDetails auth =
                (WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        sentinelService.loginSucceeded(auth.getRemoteAddress());
    }
}

