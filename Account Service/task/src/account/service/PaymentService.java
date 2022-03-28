package account.service;

import account.exception.ChangeSalaryException;
import account.exception.EmailNotFoundException;
import account.exception.UniquePeriodSalaryException;
import account.model.Payment;
import account.model.User;
import account.view.UserPaymentRepresentation;
import account.repos.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentService {

    private final SimpleDateFormat formatter = new SimpleDateFormat("MM-yyyy");
    private final SimpleDateFormat month_year = new SimpleDateFormat("MMMMMMMMM-yyyy", Locale.ENGLISH);

    private final PaymentRepository paymentRepository;
    private final UserService userService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, UserService userService) {
        this.paymentRepository = paymentRepository;
        this.userService = userService;
    }

    @Transactional
    public void savePayments(List<Payment> payments) {
        for (int i = 0, paymentsSize = payments.size(); i < paymentsSize; i++) {
            Payment payment = payments.get(i);
            String employee = payment.getEmployee();
            String period = payment.getPeriod();
            if (paymentRepository.existDublicateSalary(employee, period))
                throw new UniquePeriodSalaryException(String.valueOf(i));

            payment.setUser(userService.findUserByEmail(payment.getEmployee()));
        }
        paymentRepository.saveAll(payments);
    }

    @Transactional
    public void changeSalary(Payment payment) {
        Optional<Payment> oldPayment = paymentRepository.findByEmployeeAndPeriod(payment.getEmployee(), payment.getPeriod());
        if (oldPayment.isEmpty()) {
            throw new ChangeSalaryException();
        }
        oldPayment.get().setSalary(payment.getSalary());
        paymentRepository.save(oldPayment.get());
    }

    public ResponseEntity<Object> getPayments(String email, String inputPeriod) {
        Optional<String> optionalS = Optional.ofNullable(inputPeriod);
        return optionalS
                .<ResponseEntity<Object>>map(period -> ResponseEntity.ok(getPaymentsByEmailAndPeriod(email, period)))
                .orElseGet(() -> ResponseEntity.ok(getPaymentsByEmail(email)));
    }

    public List<UserPaymentRepresentation> getPaymentsByEmail(String email) {
        Optional<List<Payment>> payments = paymentRepository.findAllByEmployeeIgnoreCaseOrderByPeriodDesc(email);
        List<Payment> paymentsList = payments.orElse(Collections.emptyList());
        List<UserPaymentRepresentation> paymentData = new LinkedList<>();
        paymentsList.forEach(payment -> {
            User user = userService.findUserByEmail(payment.getEmployee());
            paymentData.add(new UserPaymentRepresentation(
                    user.getName(),
                    user.getLastname(),
                    formatPeriod(payment.getPeriod()),
                    formatSalary(payment.getSalary())
            ));
        });
        return paymentData;
    }

    public UserPaymentRepresentation getPaymentsByEmailAndPeriod(String email, String period) {
        Optional<Payment> payment = paymentRepository.findByEmployeeAndPeriod(email, period);
        return payment.map(queriedPayment -> {
            User user = userService.findUserByEmail(payment.get().getEmployee());
            return new UserPaymentRepresentation(
                    user.getName(),
                    user.getLastname(),
                    formatPeriod(payment.get().getPeriod()),
                    formatSalary(payment.get().getSalary())
            );
        }).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Payment found"));
    }

    public String formatPeriod(String period) {
        try {
            Date date = formatter.parse(period);
            return month_year.format(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Date!");
        }
    }

    public String formatSalary(Long salary) {
        try {
            return String.format("%d dollar(s) %d cent(s)", salary / 100, salary % 100);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Salary!");
        }
    }

//    public boolean isPaymentUnique(String employee, String period) {
//        return paymentRepository.findPayrollByEmployeeIgnoreCaseAndPeriod(employee, period).isEmpty();
//    }

}
