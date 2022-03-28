package account.service;

import account.exception.*;
import account.model.User;
import account.repos.UserRepository;
import account.view.UserAdminRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService {

    private static final Map<String, String> ROLES = Map.of(
            "USER", "ROLE_USER",
            "ADMINISTRATOR", "ROLE_ADMINISTRATOR",
            "ACCOUNTANT", "ROLE_ACCOUNTANT"
    );

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User findUserByEmail(String email) throws ResponseStatusException {
        return userRepository
                .findUserByEmailIgnoreCase(email)
                .orElseThrow(UserNotFoundException::new);
    }


    public UserAdminRepresentation registerUser(User newUser) {
        Optional<User> userOptional = userRepository.findUserByEmailIgnoreCase(newUser.getEmail());
        if (userOptional.isPresent())
            throw new UserExistException();

        return new UserAdminRepresentation(userRepository.save(
                getUserModelWithAddedRoles(userRepository.findAll().isEmpty(), newUser)));
    }

    public User getUserModelWithAddedRoles(boolean isFirstUser, User appUser) {
        appUser.addRole(isFirstUser ? ROLES.get("ADMINISTRATOR") : ROLES.get("USER"));
        appUser.setEmail(appUser.getEmail().toLowerCase(Locale.ROOT));
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUser;
    }

    public Map<String, String> changePassword(String email, String newPassword) {
        Optional<User> appUserOptional = userRepository.findUserByEmailIgnoreCase(email);
        User appUser = appUserOptional.orElseThrow(UserNotFoundException::new);

        if (passwordEncoder.matches(newPassword, appUser.getPassword())) {
            throw new ErrorChangePasswordException();
        }
        appUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(appUser);
        return Map.of(
                "status", "The password has been updated successfully",
                "email", appUser.getEmail()
        );
    }

    public List<UserAdminRepresentation> getAllUsersAndInfo() {
        return userRepository
                .findAll()
                .stream()
                .map(UserAdminRepresentation::new)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public String checkRoleUpdateRequestValidity(User appUser, String operation, String role) {
        if (!ROLES.containsKey(role)) {
            return "Role not found";
        }
        if (operation.equals("GRANT") && isCombiningRoles(appUser.getRoles(), role)) {
            return "The user cannot combine administrative and business roles!";
        }
        if (operation.equals("GRANT") && appUser.getRoles().contains(ROLES.get(role))) {
            return "User already has the role";
        }
        if (operation.equals("REMOVE") && role.equals("ADMINISTRATOR")) {
            return "Can't remove ADMINISTRATOR role!";
        }
        if (operation.equals("REMOVE") && !appUser.getRoles().contains(ROLES.get(role))) {
            return "The user does not have a role!";
        }
        if (operation.equals("REMOVE") && appUser.getRoles().size() == 1) {
            return "The user must have at least one role!";
        }
        return null;
    }

    public UserAdminRepresentation updateUserRole(Map<String, String> roleMap) throws ResponseStatusException {
        if (!ROLES.containsKey(roleMap.get("role"))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        }
        User appUser = userRepository.findUserByEmailIgnoreCase(roleMap.get("user"))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        String operation = roleMap.get("operation");
        String role = roleMap.get("role");
        String errorMessage = checkRoleUpdateRequestValidity(appUser, operation, role);
        if (errorMessage != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }
        appUser.modifyRole(operation, ROLES.get(role));
        userRepository.save(appUser);
        return new UserAdminRepresentation(appUser);
    }

    public boolean isCombiningRoles(List<String> assignedRoles, String requestedRole) {
        return isCombiningAdminWithBusinessRole(assignedRoles, requestedRole) ||
                isCombiningBusinessRoleWithAdmin(assignedRoles, requestedRole);
    }

    private boolean isCombiningBusinessRoleWithAdmin(List<String> assignedRoles, String requestedRole) {
        return assignedRoles.contains(ROLES.get("ADMINISTRATOR")) &&
                (requestedRole.equals("ACCOUNTANT") || requestedRole.equals("USER"));
    }

    private boolean isCombiningAdminWithBusinessRole(List<String> assignedRoles, String requestedRole) {
        return (assignedRoles.contains(ROLES.get("USER")) || assignedRoles.contains(ROLES.get("ACCOUNTANT")))
                && requestedRole.equals("ADMINISTRATOR");
    }


    public Map<String, String> deleteUserByEmail(String email) throws ResponseStatusException {
        User appUser = userRepository.findUserByEmailIgnoreCase(email)
                .orElseThrow(UserNotFoundException::new);

        if (appUser.getRoles().contains(ROLES.get("ADMINISTRATOR"))) {
            throw new DeleteAdministratorRoleException();
        }
        userRepository.delete(appUser);
        return Map.of(
                "user", email,
                "status", "Deleted successfully!"
        );
    }
}
