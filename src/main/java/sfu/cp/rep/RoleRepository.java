package sfu.cp.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import sfu.cp.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
