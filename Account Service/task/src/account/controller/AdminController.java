package account.controller;

import account.exception.RemoveAdminException;
import account.model.User;
import account.service.UserDetailsServiceImpl;
import account.view.ChangeUserRoleRepresentation;
import com.sun.istack.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("api/admin")
@RolesAllowed({ "ROLE_ADMINISTRATOR" })
@Validated
public class AdminController {

    private final UserDetailsServiceImpl userService;

    public AdminController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    /*  PUT api/admin/user/role sets the roles;
        DELETE api/admin/user deletes users;
        GET api/admin/user obtains information about all users; the information should not be sensitive. */

    @PutMapping("/user/role")
    public User changeUserRole(@RequestBody @Valid @Nullable ChangeUserRoleRepresentation changeUserRoleRepresentation) {
        return userService.updateUserRole(changeUserRoleRepresentation);
    }

    @DeleteMapping(value = {"/{email}", "/user"})
    public ResponseEntity<Map<String, String>> deleteUsers(@PathVariable(name = "email", required = false) String email,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        if (email.toLowerCase(Locale.ROOT).equals(userDetails.getUsername().toLowerCase(Locale.ROOT))) {
            throw new RemoveAdminException();
        }
        return ResponseEntity.ok().body(userService.deleteUser(email));
    }

    //@PreAuthorize("{hasRole('ROLE_ADMIN')}")
    @GetMapping("/user")
    public List<User> getInfo() {
        return userService.getAllUsers();
    }
}
