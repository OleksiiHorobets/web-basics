package ua.gorobeos.jwtauth.service;

import ua.gorobeos.jwtauth.dto.auth.AuthenticationRequest;
import ua.gorobeos.jwtauth.dto.auth.RegistrationRequest;

public interface AuthService {

  String register(RegistrationRequest registrationRequest);

  String authenticate(AuthenticationRequest authenticationRequest);
}
