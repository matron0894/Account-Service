package account.controller;

import account.domain.User;
import account.exception.MyEntityNotFoundException;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/empl")
public class EmplController {

    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/payment")
    public User getPayment(@AuthenticationPrincipal UserDetails auth) {
        String username = auth.getUsername();
        return userService.findUser(username)
                .orElseThrow(() -> new MyEntityNotFoundException(username));

    }
}
