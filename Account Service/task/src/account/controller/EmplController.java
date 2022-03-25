package account.controller;

import account.service.PaymentService;
import account.validation.DateFormatValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empl")
@Validated
public class EmplController {

    @Autowired
    private PaymentService paymentService;

    // GET api/empl/payment - gives access to the payroll of an employee.
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/payment")
    public ResponseEntity<Object> getPayment(@AuthenticationPrincipal UserDetails auth,
                                             @DateFormatValidation
                                             @RequestParam(value = "period", required = false)
                                                     String period) {

        return paymentService.getPayments(auth.getUsername(), period);
    }
}