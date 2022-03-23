package account.controller;

import account.exception.EmailNotFoundException;
import account.model.User;
import account.security.UserDetailsServicesImpl;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empl")
public class EmplController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServicesImpl userDetailsServices;

    // GET api/empl/payment - gives access to the payroll of an employee.
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/payment")
    public User getPayment(@AuthenticationPrincipal UserDetails auth) {
        return userDetailsServices.getUserByEmail(auth.getUsername());
    }
}
