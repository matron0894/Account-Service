package account.controller;

import account.exception.ErrorChangePasswordException;
import account.exception.UsernameFoundException;
import account.model.NewPassword;
import account.model.User;
import account.security.UserDetailsServicesImpl;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserDetailsServicesImpl userDetailsServices;

    @Autowired
    public AuthController(UserService userService,
                          UserDetailsServicesImpl userDetailsServices) {
        this.userService = userService;
        this.userDetailsServices = userDetailsServices;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@Valid @RequestBody User user) {
        Optional<User> newUser = userService.findUserByEmail(user.getEmail());
        if (newUser.isPresent()) throw new UsernameFoundException();
        userService.addNewUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/changepass")
    public ResponseEntity<Map<String, String>> changePass(@AuthenticationPrincipal UserDetails auth,
                                                          @Valid @RequestBody NewPassword changePass) {
        User user = userDetailsServices.getUserByEmail(auth.getUsername());
        boolean isChange = userService.changePassword(user, changePass.getNew_password());
        if (!isChange) {
            throw new ErrorChangePasswordException();
        }
        HashMap<String, String> reply = new HashMap<>();
        reply.put("email", auth.getUsername());
        reply.put("status", "The password has been updated successfully");
        return new ResponseEntity<>(reply, HttpStatus.OK);
    }
}
