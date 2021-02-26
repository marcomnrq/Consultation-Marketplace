package com.marcomnrq.consultation.controller;

import com.marcomnrq.consultation.resource.authentication.*;
import com.marcomnrq.consultation.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpResource registrationRequest){
        authenticationService.signUp(registrationRequest);
        return new ResponseEntity<>("User registration was succesfull", HttpStatus.OK);
    }

    @PostMapping("/sign-in")
    public AuthenticationResource signIn(@RequestBody SignInResource loginRequest){
        return authenticationService.signIn(loginRequest);
    }

    @PostMapping("/refresh-token")
    public AuthenticationResource refreshToken(@Valid @RequestBody RefreshTokenResource refreshTokenRequest){
        //String email = principal.getName();
        //refreshTokenRequest.setEmail(email);

        return authenticationService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<String> signOut(@Valid @RequestBody RefreshTokenResource refreshTokenRequest){
        authenticationService.signOut(refreshTokenRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Refresh token has been deleted");
    }

    @GetMapping("/verification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable(name = "token") String token){
        authenticationService.verifyAccount(token);
        return new ResponseEntity<>("User account has been activated", HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordResource forgotPasswordRequest){
        authenticationService.forgotPassword(forgotPasswordRequest);
        return new ResponseEntity<>("Petition sent", HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordResource resetPasswordRequest){
        authenticationService.resetPassword(resetPasswordRequest);
        return new ResponseEntity<>("Password changed", HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordResource changePasswordRequest, Principal principal){
        String username = principal.getName();
        authenticationService.changePassword(username, changePasswordRequest);
        return new ResponseEntity<>("Password changed", HttpStatus.OK);
    }

}
