package account.repos;

import account.model.Logging;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Logging, Long> {

}
