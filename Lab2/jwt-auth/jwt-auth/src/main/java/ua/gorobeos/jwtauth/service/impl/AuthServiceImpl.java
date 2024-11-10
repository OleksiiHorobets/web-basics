package ua.gorobeos.jwtauth.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.gorobeos.jwtauth.dto.auth.AuthenticationRequest;
import ua.gorobeos.jwtauth.dto.auth.RegistrationRequest;
import ua.gorobeos.jwtauth.entity.User;
import ua.gorobeos.jwtauth.exception.UserAlreadyExistsException;
import ua.gorobeos.jwtauth.repository.UserRepository;
import ua.gorobeos.jwtauth.service.AuthService;
import ua.gorobeos.jwtauth.utils.JwtTokenUtils;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final static String DEFAULT_ROLE = "ROLE_USER";

  private final UserRepository userRepository;
  private final JwtTokenUtils jwtTokenManager;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  @Override
  @Transactional
  public String register(RegistrationRequest registrationRequest) {
    checkIfAlreadyExists(registrationRequest);

    var registeredUser = userRepository.save(buildUser(registrationRequest, DEFAULT_ROLE));

    return jwtTokenManager.generateToken(registeredUser);
  }

  @Override
  @Transactional
  public String authenticate(AuthenticationRequest authRequest) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
    );

    User user = (User) auth.getPrincipal();

    return jwtTokenManager.generateToken(user);
  }

  private void checkIfAlreadyExists(RegistrationRequest registrationRequest) {

    if (userRepository.existsByUsername(registrationRequest.getUsername())) {
      throw new UserAlreadyExistsException("User with such username already exists: " + registrationRequest.getUsername());
    }
  }

  private User buildUser(RegistrationRequest registrationRequest, String role) {
    return User.builder()
        .username(registrationRequest.getUsername())
        .firstName(registrationRequest.getFirstName())
        .lastName(registrationRequest.getLastName())
        .password(passwordEncoder.encode(registrationRequest.getPassword()))
        .role(role)
        .build();
  }
}