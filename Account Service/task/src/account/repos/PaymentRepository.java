package account.repos;

import account.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
//    @Query("select c from User c join fetch c.topic where c.id=:id")
//    Optional<Payment> findWithJoinFetch(long id);


    Optional<Payment> findByEmployeeAndPeriod(String employee, String period);

    Optional<List<Payment>> findAllByEmployeeOrderByPeriodDesc(String employee);

    //    @Query("select case when count(c) > 0 then true else false" +
//            " end from Car c where lower(c.model) like lower(:model)")
//    Boolean existPaymentByEmployeeAndByPeriod(String employee, String period);

    @Query("SELECT COUNT(c) > 0 FROM Payment c WHERE c.employee = :empl AND c.period = :period")
    Boolean existDublicateSalary(@Param("empl") String employee, @Param("period") String period);
}
