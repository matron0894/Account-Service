package account.repos;

import account.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long > {

    Optional<User> findUserByEmailIgnoreCase(String username);
    // Boolean existsByEmail(String email);

    Boolean existsByEmail(String email);

    /*to update the number of failed login attempts for a user based on his email*/
    @Modifying
    @Query("UPDATE User u SET u.failedAttempt = :fails WHERE u.email = :email")
    int updateFailedAttemptsForUser(@Param("fails") Integer failAttempts, @Param("email") String email);
}
