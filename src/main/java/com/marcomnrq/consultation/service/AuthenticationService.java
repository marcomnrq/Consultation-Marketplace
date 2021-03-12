package com.marcomnrq.consultation.service;

import com.marcomnrq.consultation.domain.model.*;
import com.marcomnrq.consultation.domain.repository.*;
import com.marcomnrq.consultation.exception.CustomException;
import com.marcomnrq.consultation.resource.authentication.*;
import com.marcomnrq.consultation.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final RefreshTokenService refreshTokenService;

    private final MailService mailService;

    private final TotpService totpService;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final PlanRepository planRepository;

    private final SentinelService sentinelService;

    @Transactional
    public void signUp(SignUpResource signUpResource) {

        // Creating a new user based of registration dto
        User user = new User();
        user.setEmail(signUpResource.getEmail());
        user.setFirstName(signUpResource.getFirstName());
        user.setLastName(signUpResource.getLastName());
        user.setPassword(passwordEncoder.encode(signUpResource.getPassword()));
        user.setGender(Gender.OTHER);
        user.setPlan(planRepository.findByName("PLAN_FREE")
                .orElseThrow(() -> new CustomException("Plan not found")));
        user.setUsing2FA(false);
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new CustomException("Role user not found"))));
        user.setIsProfessional(false);
        user.setEnabled(false);

        // Saving the new user to the database
        userRepository.save(user);

        // User e-mail verification
        String token = generateVerificationToken(user);
        NotificationEmail notificationEmail = new NotificationEmail();
        notificationEmail.setRecipient(user.getEmail());
        notificationEmail.setSubject("Activación de cuenta");
        notificationEmail.setFullName(user.getFirstName());
        notificationEmail.setButtonLink("https://google.com");
        notificationEmail.setButtonText("Activa tu cuenta");
        notificationEmail.setBody("Gracias por registrarte, tu link de activación usa el siguiente token: " + token);
        mailService.sendMail(notificationEmail);
    }

    public AuthenticationResource signIn(SignInResource loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new CustomException("User not found"));
        if(!user.getUsing2FA()) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            sentinelService.verifyDevice(authentication, ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
            // Returning
            String token = jwtProvider.generateToken(authentication);
            return new AuthenticationResource(
                    token,
                    refreshTokenService.generateRefreshToken().getToken(),
                    Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()),
                    loginRequest.getEmail(),
                    false);
        }else{
            // User is using two factor authentication
            throw new CustomException("User has 2FA enabled");
        }
    }

    public AuthenticationResource signInWithTotp(TotpSignInResource loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new CustomException("User not found"));
        if(user.getUsing2FA()) {
            if(totpService.verifyCode(loginRequest.getTotp(), user.getSecretKey())) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                sentinelService.verifyDevice(authentication, ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
                // Returning
                String token = jwtProvider.generateToken(authentication);
                return new AuthenticationResource(
                        token,
                        refreshTokenService.generateRefreshToken().getToken(),
                        Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()),
                        loginRequest.getEmail(),
                        true);
            } else{
                throw new CustomException("2FA code is not valid");
            }
        }else{
            // User is using two factor authentication
            throw new CustomException("User does not have 2FA enabled");
        }
    }

    public AuthenticationResource refreshToken(RefreshTokenResource refreshTokenRequest){
        // Refresh the token
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithEmail(refreshTokenRequest.getEmail());
        //TODO: validate user email with the refresh token
        return new AuthenticationResource(
                token,
                refreshTokenRequest.getRefreshToken(),
                Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()),
                refreshTokenRequest.getEmail(),
                false);
    }

    public void signOut(RefreshTokenResource refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
    }

    public void verifyAccount(String token) {
        // Checking token validation
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new CustomException("Invalid token"));

        // Activating user's account
        User user = verificationToken.get().getUser();
        if (user.getEnabled()){
            throw new CustomException("Account has already been activated");
        } else {
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    public void forgotPassword(ForgotPasswordResource forgotPasswordRequest) {
        // Getting user from email parameter
        Optional<User> user = userRepository.findByEmail(forgotPasswordRequest.getEmail());
        if(!user.isEmpty()){
            // Generate password reset token
            String token = UUID.randomUUID().toString();
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setToken(token);
            passwordResetToken.setUser(user.get());
            passwordResetTokenRepository.save(passwordResetToken);

            // TODO: Send an email to the user with the token
        }
    }

    public void resetPassword(ResetPasswordResource resetPasswordRequest) {
        // Validating the token
        String token = resetPasswordRequest.getToken();
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if(!passwordResetToken.isEmpty()){
            // Changing user password
            passwordResetToken.get().getUser().setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
            userRepository.save(passwordResetToken.get().getUser());
        }else{
            throw new CustomException("Invalid token");
        }
    }

    public void changePassword(String email, ChangePasswordResource changePasswordRequest) {
        // Validating user and passwords
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isEmpty()){
            if(changePasswordRequest.getNewPassword().equals(changePasswordRequest.getNewPassword())){
                // Changing user password
                user.get().setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
                userRepository.save(user.get());
            }else{
                throw new CustomException("Invalid password");
            }
        }else{
            throw new CustomException("Invalid user");
        }

    }

    public String enable2fa(String email) {
        // Validating user
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(!optionalUser.isEmpty()){
            User user = optionalUser.get();
            if(!user.getUsing2FA()){
                // Generate Secret Key
                String secret =totpService.generateSecret();
                user.setSecretKey(secret);
                user.setUsing2FA(true);
                userRepository.save(user);
                return totpService.getUriForImage(secret, user.getEmail());
            }else{
                throw new CustomException("2FA has already been enabled");
            }
        }else{
            throw new CustomException("Invalid user");
        }
    }

    public void disable2fa(String email) {
        //TODO: implement account disabling 2fa
    }

    public String generateVerificationToken(User user) {
        // Generating a new token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        // Saving the token and returning it
        verificationTokenRepository.save(verificationToken);
        return token;
    }
}
