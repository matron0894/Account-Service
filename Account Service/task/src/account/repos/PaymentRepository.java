package account.repos;

import account.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface PaymentRepository extends JpaRepository<Payment, Long> {
//    @Query("select c from User c join fetch c.topic where c.id=:id")
//    Optional<Payment> findWithJoinFetch(long id);

    boolean findByPeriodEquals(String date);

    Optional<Payment> findByEmployeeAndPeriod(String employee, String period);
    Optional<List<Payment>> findAllPaymentsById(Long id);

}
