package account.controller;

import account.service.PaymentService;
import account.service.UserService;
import account.validation.DateFormatValidation;
import account.view.UserPaymentRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empl")
@Validated
public class EmplController {

    Logger log = LoggerFactory.getLogger(UserService.class);
    private final PaymentService paymentService;

    @Autowired
    public EmplController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // GET api/empl/payment - gives access to the payroll of an employee.
//    @GetMapping(value = "/payment")
//    public List<UserPaymentRepresentation> getPayment(@AuthenticationPrincipal UserDetails auth) {
//        return paymentService.getPayments(auth.getUsername());
//    }
//
//    @GetMapping(value = "/payment", params = "period")
//    public UserPaymentRepresentation getPayment(@AuthenticationPrincipal UserDetails auth,
//                                                @DateFormatValidation
//                                                @RequestParam String period) {
//        return paymentService.getPayments(auth.getUsername(), period);
//    }


    @GetMapping("/payment")
    @ResponseBody
    public ResponseEntity<?> getAllPayments(@DateFormatValidation
                                            @RequestParam(name = "period", required = false) String period,
                                            @AuthenticationPrincipal UserDetails auth) {
        log.trace("!!!!!!!!!!!!!!!!!!  get period: " + period + ", input data: " + auth.getUsername());
        if (period == null) {
            List<UserPaymentRepresentation> payments = paymentService.getPaymentsByPeriod(auth.getUsername());
            return new ResponseEntity<>(payments, HttpStatus.OK);
        }
        UserPaymentRepresentation payment = paymentService.getPaymentsByPeriod(auth.getUsername(), period);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }
}