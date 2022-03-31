package account.repos;

import account.model.Logging;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Logging, Long> {

    List<Logging> findAll();
}
