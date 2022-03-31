package account.controller;

import account.model.Event;
import account.model.User;
import account.service.LoggingService;
import account.service.UserService;
import account.view.NewPassword;
import account.view.UserAdminRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private LoggingService loggingService;

    @PostMapping("/signup")
    public UserAdminRepresentation signup(@RequestParam(required = false) @AuthenticationPrincipal UserDetails auth,
                                          @RequestBody @Valid User user) {
        Optional<UserDetails> object = Optional.ofNullable(auth);
        String author = object.isPresent() ? auth.getUsername() : "Anonymous";
        loggingService.log(Event.CREATE_USER,
                author,
                user.getEmail(),
                "/api/auth/signup");
        return userService.registerUser(user);
    }


    //    @PreAuthorize("isAuthenticated()")
    @PostMapping("/changepass")
    public ResponseEntity<Map<String, String>> changePass(@AuthenticationPrincipal UserDetails auth,
                                                          @Valid @RequestBody NewPassword newPassword) {
        User user = userService.changePassword(auth.getUsername(), newPassword.getNew_password());

        loggingService.log(Event.CHANGE_PASSWORD,
                auth.getUsername(),
                user.getEmail(),
                "/api/auth/changepass");
        return new ResponseEntity<>(Map.of(
                "status", "The password has been updated successfully",
                "email", user.getEmail()), HttpStatus.OK);
    }
}
