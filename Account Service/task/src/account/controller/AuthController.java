package account.controller;

import account.model.User;
import account.service.UserService;
import account.view.NewPassword;
import account.view.UserAdminRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserAdminRepresentation signup(@RequestBody @Valid User user) {
        return userService.registerUser(user);
    }


//    @PreAuthorize("isAuthenticated()")
    @PostMapping("/changepass")
    public ResponseEntity<Map<String, String>> changePass(@AuthenticationPrincipal UserDetails auth,
                                                          @Valid @RequestBody NewPassword newPassword) {
        return ResponseEntity.ok().body(
                userService.changePassword(auth.getUsername(), newPassword.getNew_password())
        );
    }
}
