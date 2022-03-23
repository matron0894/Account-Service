package account.controller;

import account.model.User;
import account.security.UserDetailsServicesImpl;
import account.service.UserService;
import account.validation.DateFormatValidation;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserService userService;

    @Autowired
    private UserDetailsServicesImpl userDetailsServices;

    // GET api/empl/payment - gives access to the payroll of an employee.
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/payment")
    public User getPayment(@AuthenticationPrincipal UserDetails auth,
                           @RequestParam(required = false)
                           @DateFormatValidation(message = "Wrong date!")
                                   String period) {
        return userDetailsServices.getUserByEmail(auth.getUsername());
    }
}
