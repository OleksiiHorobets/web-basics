package ua.gorobeos.jwtauth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ua.gorobeos.jwtauth.dto.ApiErrorResponse.UserDetailsDto;
import ua.gorobeos.jwtauth.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

  @Mapping(source = "id", target = "id")
  UserDetailsDto toUserDetailsDto(User entity);

}
