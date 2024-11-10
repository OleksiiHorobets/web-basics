package ua.gorobeos.jwtauth.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.gorobeos.jwtauth.dto.ApiErrorResponse.UserDetailsDto;
import ua.gorobeos.jwtauth.mapper.UserMapper;
import ua.gorobeos.jwtauth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserDetails loadUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username not found: %s".formatted(username)));
  }

  public Page<UserDetailsDto> getUsers(Pageable pageable) {
    return userRepository.findAll(pageable)
        .map(userMapper::toUserDetailsDto);
  }

  public Optional<UserDetailsDto> getUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .map(userMapper::toUserDetailsDto);
  }

}
