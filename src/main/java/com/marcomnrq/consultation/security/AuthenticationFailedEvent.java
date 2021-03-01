package com.marcomnrq.consultation.security;

import com.marcomnrq.consultation.service.SentinelService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationFailedEvent implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final SentinelService sentinelService;

    // This class is an event listener for failed authentication

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        WebAuthenticationDetails auth =
                (WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        sentinelService.loginFailed(auth.getRemoteAddress());
    }
}
