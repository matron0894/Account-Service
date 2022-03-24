package account.service;

import account.exception.ChangeSalaryException;
import account.exception.EmailNotFoundException;
import account.exception.UniquePeriodSalaryException;
import account.exception.UsernameFoundException;
import account.model.Payment;
import account.model.User;
import account.repos.PaymentRepository;
import account.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }


    public void savePayments(List<Payment> payments) {
        for (int i = 0, paymentsSize = payments.size(); i < paymentsSize; i++) {
            Payment payment = payments.get(i);
            String employee = payment.getEmployee();
            String period = payment.getPeriod();
            if (paymentRepository.existDublicateSalary(employee, period))
                throw new UniquePeriodSalaryException(String.valueOf(i));

            Optional<User> user = userRepository.findUserByEmailIgnoreCase(payment.getEmployee());
            payment.setUser(user.orElseThrow(EmailNotFoundException::new));
        }
        paymentRepository.saveAll(payments);
    }


    public void changeSalary(Payment payment) {
        Optional<Payment> oldPayment = paymentRepository.findByEmployeeAndPeriod(payment.getEmployee(), payment.getPeriod());
        if (oldPayment.isEmpty()) {
            throw new ChangeSalaryException();
        }
        oldPayment.get().setSalary(payment.getSalary());
        paymentRepository.save(oldPayment.get());
    }

    public List<Payment> getAllPaymentByEmployee(String employee) {
        Optional<List<Payment>> payments = paymentRepository.findAllByEmployeeOrderByPeriodDesc(employee);
        return payments.orElse(Collections.emptyList());
    }

    public Payment getPaymentByEmployeeAndPeriod(String employee, String period) {
        Optional<Payment> payment = paymentRepository.findByEmployeeAndPeriod(employee, period);
        return payment.orElseThrow(UsernameFoundException::new);
    }

}
