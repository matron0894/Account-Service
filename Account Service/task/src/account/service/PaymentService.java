package account.service;

import account.exception.UniquePeriodSalaryException;
import account.model.Payment;
import account.model.User;
import account.repos.PaymentRepository;
import account.view.UserPaymentRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final SimpleDateFormat formatter = new SimpleDateFormat("MM-yyyy");
    private final SimpleDateFormat month_year = new SimpleDateFormat("MMMMMMMMM-yyyy", Locale.ENGLISH);

    Logger log = LoggerFactory.getLogger(UserService.class);

    private final PaymentRepository paymentRepository;
    private final UserService userService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, UserService userService) {
        this.paymentRepository = paymentRepository;
        this.userService = userService;
    }


    public List<UserPaymentRepresentation> getPaymentsByPeriod(String email) {
        log.info("!!!!!!!!!!!!!!!!!!!!!!!   Email: " + email);
        List<Payment> allPayments = paymentRepository.findAllByEmployeeIgnoreCaseOrderByPeriodDesc(email);
        /*List<UserPaymentRepresentation> representations = new ArrayList<>();
        if (paymentsList == null) {
            return representations;
        }
        User user = userService.findUserByEmail(email);
        String name = user.getName();
        String lastname = user.getLastname();
        for (Payment payment : paymentsList) {
            representations.add(new UserPaymentRepresentation(
                    name,
                    lastname,
                    formatSalary(payment.getSalary()),
                    formatSalary(payment.getSalary())));
        }
        return representations;*/

        List<UserPaymentRepresentation> result = allPayments.stream()
                .map(payment -> {
                    UserPaymentRepresentation dto = new UserPaymentRepresentation();
                    dto.setPeriod(formatPeriod(payment.getPeriod()));
                    dto.setSalary(formatSalary(payment.getSalary()));
                    dto.setName(payment.getUser().getName());
                    dto.setLastname(payment.getUser().getLastname());
                    return dto;
                })
                .collect(Collectors.toList());
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!" + result);
        return result;
    }

    public UserPaymentRepresentation getPaymentsByPeriod(String email, String period) {
        Payment payment = paymentRepository.findByEmployeeAndPeriod(email, period)
                .stream()
                .findFirst()
                .get();

        User user = userService.findUserByEmail(email);
        String name = user.getName();
        String lastname = user.getLastname();
        return new UserPaymentRepresentation(name,
                lastname,
                formatPeriod(payment.getPeriod()),
                formatSalary(payment.getSalary()));

     /*   return payment.map(queriedPayment -> {
            User user = userService.findUserByEmail(payment.get().getEmployee());
            return new UserPaymentRepresentation(
                    user.getName(),
                    user.getLastname(),
                    formatPeriod(payment.get().getPeriod()),
                    formatSalary(payment.get().getSalary())
            );
        }).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Payment found"));*/
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
        Payment oldPayment = paymentRepository.findByEmployeeAndPeriod(payment.getEmployee(), payment.getPeriod())
                .stream()
                .findFirst()
                .get();
        oldPayment.setSalary(payment.getSalary());
        paymentRepository.save(oldPayment);
    }


//    public boolean isPaymentUnique(String employee, String period) {
//        return paymentRepository.findPayrollByEmployeeIgnoreCaseAndPeriod(employee, period).isEmpty();
//    }

}
