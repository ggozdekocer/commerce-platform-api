package tr.com.java.commerceplatform.user.repository;

import tr.com.java.commerceplatform.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
