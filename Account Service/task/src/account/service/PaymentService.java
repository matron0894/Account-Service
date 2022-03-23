package account.service;

import account.exception.ChangeSalaryException;
import account.exception.EmailNotFoundException;
import account.model.Payment;
import account.model.User;
import account.repos.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserService userService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, UserService userService) {
        this.paymentRepository = paymentRepository;
        this.userService = userService;
    }


    public void savePayments(List<Payment> payments) {
        Optional<User> user;
        for (int i = 0; i < payments.size(); i++) {
            user = userService.findUser(payments.get(i).getEmployee());
            payments.get(i).setUser(user.orElseThrow(EmailNotFoundException::new));
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
}
