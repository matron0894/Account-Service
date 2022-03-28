package account.controller;

import account.model.Payment;
import account.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/acct", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {
    /*POST api/acct/payments uploads payrolls;
    PUT api/acct/payments changes the salary of a specific user;
   */

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;

    }

    @PostMapping("/payments")
    public ResponseEntity<Map<String, String>> uploadPayments(@RequestBody List<@Valid Payment> payments) {
        paymentService.savePayments(payments);
        return new ResponseEntity<>(Map.of("status", "Added successfully!"), HttpStatus.OK);
    }


    @PutMapping("/payments")
    public ResponseEntity<Map<String, String>> changeSalary(@Valid @RequestBody Payment payment) {
        paymentService.changeSalary(payment);
        return new ResponseEntity<>(Map.of("status", "Updated successfully!"), HttpStatus.OK);
    }




}
