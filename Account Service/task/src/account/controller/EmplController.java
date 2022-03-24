package account.controller;

import account.model.Payment;
import account.service.PaymentService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/empl")
@Validated
public class EmplController {

    @Autowired
    private PaymentService paymentService;

    private final SimpleDateFormat formatter = new SimpleDateFormat("MM-yyyy");
    private final SimpleDateFormat month_year = new SimpleDateFormat("MMMMMMMMM-yyyy", Locale.ENGLISH);

    // GET api/empl/payment - gives access to the payroll of an employee.
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/payment")
    public ResponseEntity getPayment(@AuthenticationPrincipal UserDetails auth,
                                     @Pattern(regexp = "\\d{2}-\\d{4}", message = "Wrong date!")
                                     @RequestParam(value = "period", required = false)
                                             String period) throws ParseException {

        if (period == null) {
            List<Payment> allPayments = paymentService.getAllPaymentByEmployee(auth.getUsername());
            List<Map<String, Object>> result = new LinkedList<>();
            for (Payment p : allPayments) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("name", p.getUser().getName());
                map.put("lastname", p.getUser().getLastname());
                Date date = formatter.parse(p.getPeriod());
                map.put("period", month_year.format(date));
                map.put("salary", String.format("%d dollar(s) %d cent(s)", p.getSalary() / 100, p.getSalary() % 100));
                result.add(map);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            Payment payment = paymentService.getPaymentByEmployeeAndPeriod(auth.getUsername(), period);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("name", payment.getUser().getName());
            result.put("lastname", payment.getUser().getLastname());
            Date date = formatter.parse(payment.getPeriod());
            result.put("period", month_year.format(date));
            result.put("salary", String.format("%d dollar(s) %d cent(s)", payment.getSalary() / 100, payment.getSalary() % 100));

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

    }
}