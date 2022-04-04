package account.controller;

import account.model.User;
import account.service.LoginAttemptService;
import account.service.UserService;
import account.view.NewPassword;
import account.view.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private LoginAttemptService loggingService;

    @PostMapping("/signup")
    public UserRepresentation signup(@RequestParam(required = false) @AuthenticationPrincipal UserDetails auth,
                                     @RequestBody @Valid User user) {
        return userService.registerUser(user, auth);
    }


    //    @PreAuthorize("isAuthenticated()")
    @PostMapping("/changepass")
    public ResponseEntity<Map<String, String>> changePass(@AuthenticationPrincipal UserDetails auth,
                                                          @Valid @RequestBody NewPassword newPassword) {
        User user = userService.changePassword(auth, newPassword);
        return new ResponseEntity<>(Map.of(
                "status", "The password has been updated successfully",
                "email", user.getEmail()), HttpStatus.OK);
    }


}
