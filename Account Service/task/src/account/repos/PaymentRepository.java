package account.repos;

import account.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByEmployeeAndPeriod(String employee, String period);

    Optional<List<Payment>> findAllByEmployeeIgnoreCaseOrderByPeriodDesc(String employee);

    @Query("SELECT COUNT(c) > 0 FROM Payment c WHERE c.employee = :empl AND c.period = :period")
    Boolean existDublicateSalary(@Param("empl") String employee, @Param("period") String period);
}
