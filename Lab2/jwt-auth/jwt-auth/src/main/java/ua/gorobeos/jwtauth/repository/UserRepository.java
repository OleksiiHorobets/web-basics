package ua.gorobeos.jwtauth.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.gorobeos.jwtauth.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);
}
