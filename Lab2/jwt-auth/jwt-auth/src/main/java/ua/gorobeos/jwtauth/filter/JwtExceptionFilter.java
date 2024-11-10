package ua.gorobeos.jwtauth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.gorobeos.jwtauth.dto.ApiErrorResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

  private final ObjectMapper mapper;

  @Override
  public void doFilterInternal(@NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (JwtException jwtException) {
      setErrorResponse(HttpStatus.FORBIDDEN, response, jwtException);
      log.warn(jwtException.getMessage(), jwtException.getCause());
    }
  }

  private void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) throws IOException {
    ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
        status.value(),
        ex.getMessage()
    );

    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    response.getWriter().write(
        mapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(apiErrorResponse)
    );
  }
}
