package ua.gorobeos.jwtauth.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.gorobeos.jwtauth.dto.ApiErrorResponse.UserDetailsDto;
import ua.gorobeos.jwtauth.entity.User;
import ua.gorobeos.jwtauth.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public Page<UserDetailsDto> getUsers(@Valid @NotNull Pageable page) {
    return userService.getUsers(page);
  }

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @GetMapping("/profile")
  public Optional<UserDetailsDto> profile() {
    String currentUserUsername = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    return userService.getUserByUsername(currentUserUsername);
  }

}
