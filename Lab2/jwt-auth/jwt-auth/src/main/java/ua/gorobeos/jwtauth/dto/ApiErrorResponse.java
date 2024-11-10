package ua.gorobeos.jwtauth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.gorobeos.jwtauth.entity.User;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

  private int code;
  private String message;
  private Object data;

  public ApiErrorResponse(int code, String message) {
    this.code = code;
    this.message = message;
  }

  /**
   * DTO for {@link User}
   */
  public static record UserDetailsDto(Long id, String username, String role, String firstName, String lastName) implements Serializable {

  }
}
