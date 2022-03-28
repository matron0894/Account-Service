package account.repos;

import account.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByCode(String code);
}
