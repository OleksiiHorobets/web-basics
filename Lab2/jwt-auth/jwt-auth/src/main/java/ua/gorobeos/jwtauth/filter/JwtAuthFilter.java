package ua.gorobeos.jwtauth.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.gorobeos.jwtauth.service.UserService;
import ua.gorobeos.jwtauth.utils.JwtTokenUtils;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_BEARER_PREFIX = "Bearer ";
  private final JwtTokenUtils jwtTokenUtils;
  private final UserService userService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    getBearerToken(request).ifPresent(jwt -> {
      String username = jwtTokenUtils.getUsername(jwt);

      Optional.ofNullable(username)
          .filter(user -> isUserNotAuthenticated())
          .map(userService::loadUserByUsername)
          .filter(user -> jwtTokenUtils.isTokenValid(jwt, user))
          .ifPresent(setUpAuthToken(request));
    });
    filterChain.doFilter(request, response);
  }

  private boolean isUserNotAuthenticated() {
    return SecurityContextHolder.getContext().getAuthentication() == null;
  }

  private Optional<String> getBearerToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
        .filter(token -> token.startsWith(AUTHORIZATION_BEARER_PREFIX))
        .map(token -> token.replaceFirst(AUTHORIZATION_BEARER_PREFIX, ""));
  }

  private UsernamePasswordAuthenticationToken buildAuthToken(HttpServletRequest request, UserDetails userDetails) {
    var authToken = new UsernamePasswordAuthenticationToken(
        userDetails,
        userDetails.getPassword(),
        userDetails.getAuthorities()
    );
    authToken.setDetails(
        new WebAuthenticationDetailsSource().buildDetails(request)
    );
    return authToken;
  }

  private Consumer<UserDetails> setUpAuthToken(HttpServletRequest request) {
    return userDetails -> {
      var authToken = buildAuthToken(request, userDetails);
      SecurityContextHolder.getContext().setAuthentication(authToken);
    };
  }
}
