package account.controller;

import account.service.UserService;
import account.view.UserAdminRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("api/admin")
//@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
@Validated
public class AdminController {

     /*  PUT api/admin/user/role sets the roles;
        DELETE api/admin/user deletes users;
        GET api/admin/user obtains information about all users; the information should not be sensitive. */


    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<UserAdminRepresentation> showAllUserInfo() {
        return userService.getAllUsersAndInfo();
    }

    @DeleteMapping("/user/{email}")
    public Map<String, String> deleteUser(@PathVariable(name = "email", required = false) String email) {
        return userService.deleteUserByEmail(email.toLowerCase(Locale.ROOT));
    }

    @PutMapping("/user/role")
    public UserAdminRepresentation updateUserRole(@RequestBody Map<String, String> roleMap) {
        return userService.updateUserRole(roleMap);
    }


}
