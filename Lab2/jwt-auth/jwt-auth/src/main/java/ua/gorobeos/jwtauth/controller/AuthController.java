package ua.gorobeos.jwtauth.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.gorobeos.jwtauth.dto.auth.AuthenticationRequest;
import ua.gorobeos.jwtauth.dto.auth.AuthenticationResponse;
import ua.gorobeos.jwtauth.dto.auth.RegistrationRequest;
import ua.gorobeos.jwtauth.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @SecurityRequirement(name = "none")
  @PostMapping("/login")
  @ResponseBody
  public AuthenticationResponse login(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
    String token = authService.authenticate(authenticationRequest);

    return AuthenticationResponse.builder()
        .token(token)
        .timestamp(LocalDateTime.now())
        .build();
  }

  @SecurityRequirement(name = "none")
  @PostMapping("/registration")
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  public AuthenticationResponse register(@Valid @RequestBody RegistrationRequest registrationRequest) {
    String token = authService.register(registrationRequest);

    return AuthenticationResponse.builder()
        .token(token)
        .timestamp(LocalDateTime.now())
        .build();
  }
}
