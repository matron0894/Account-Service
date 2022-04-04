package account.controller;

import account.model.RoleOperation;
import account.service.UserService;
import account.view.RequestAccessRepresentation;
import account.view.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("api/admin")
//@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
@Validated
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<UserRepresentation> showAllUserInfo(){
        return userService.getAllUsersAndInfo();
    }

    @DeleteMapping("/user/{email}")
    public Map<String, String> deleteUser(@AuthenticationPrincipal UserDetails auth,
                                          @PathVariable(name = "email", required = false) String email) {
        return userService.deleteUserByEmail(email.toLowerCase(Locale.ROOT), auth.getUsername().toLowerCase(Locale.ROOT));
    }

    @PutMapping("/user/role")
    public UserRepresentation updateUserRole(@AuthenticationPrincipal UserDetails auth,
                                             @Valid @RequestBody RoleOperation roleOperation) {
        return userService.updateUserRole(roleOperation, auth.getUsername().toLowerCase(Locale.ROOT));
    }


    @PutMapping("/user/access")
    public Map<String, String> updateLockStatus(@Valid @RequestBody RequestAccessRepresentation representation) {
        return userService.changeLockStatus(representation.getUser(), representation.getOperation());

    }

}
