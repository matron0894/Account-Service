package account.controller;

import account.domain.User;
import account.exception.MyEntityNotFoundException;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/signup")
    public ResponseEntity<User> signup(@Valid @RequestBody User user) {
        Optional<User> newUser = userService.findUser(user.getEmail());
        if (newUser.isPresent()) throw new MyEntityNotFoundException();
        userService.addNewUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
//    @PostMapping("/changepass")
//    public void changePass() {
//
//    }


}
