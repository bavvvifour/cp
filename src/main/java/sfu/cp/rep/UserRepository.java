package sfu.cp.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import sfu.cp.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhone(String phone);
}
