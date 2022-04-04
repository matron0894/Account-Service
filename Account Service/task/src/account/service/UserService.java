package account.service;

import account.exception.*;
import account.model.Event;
import account.model.Logging;
import account.model.RoleOperation;
import account.model.User;
import account.repos.RoleGroupRepository;
import account.repos.LogRepository;
import account.repos.UserRepository;
import account.view.NewPassword;
import account.view.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    Logger log = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;
    private final RoleGroupRepository groups;
    private final UserRepository userRepository;
    private final LogRepository events;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, RoleGroupRepository groups, UserRepository userRepository, LogRepository events) {
        this.passwordEncoder = passwordEncoder;
        this.groups = groups;
        this.userRepository = userRepository;
        this.events = events;
    }


    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(LocalDate.now());
        userRepository.save(user);
        log.info(Util.getEmail());
        events.save(new Logging(Event.LOCK_USER, Util.getEmail(), String.format("Lock user %s", user.getEmail()), "/api/admin/user/access"));
    }

    public void unlock(User user) {
        user.setAccountNonLocked(true);
        user.setLockTime(null);
        userRepository.save(user);

        events.save(new Logging(Event.UNLOCK_USER, user.getEmail(), String.format("Unlock user %s", user.getEmail()), "/api/admin/user/access"));
    }

    /*
        public void increaseFailedAttempts(String email) {
            User user = findUserByEmail(email);
            int newFailAttempts = user.getFailedAttempt() + 1;
            userRepository.updateFailedAttemptsForUser(newFailAttempts, user.getEmail());
        }

        public void resetFailAttempts(String username) {
            int res = userRepository.updateFailedAttemptsForUser(0, username);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!   " + res);
        }


        public void updateFailAttempts(String email) {
            User user = findUserByEmail(email);
            if (user != null) {
                if (user.isAccountNonLocked()) {
                    if (user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
                        increaseFailedAttempts(email);
                    } else {
                        lock(user);
                        throw new LockedException("Your account has been locked due to 3 failed attempts."
                                + " It will be unlocked after 24 hours.");
                    }
                } else {
                    if (unlockWhenTimeExpired(user)) {
                        throw new LockedException("Your account has been unlocked. Please try to login again.");
                    }
                }
            }
        }

         public boolean unlockWhenTimeExpired(User user) {
            long lockTimeInMillis = user.getLockTime().getDayOfMonth();
            long currentTimeInMillis = System.currentTimeMillis();

            if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
                user.setAccountNonLocked(true);
                user.setLockTime(null);
                user.setFailedAttempt(0);
                userRepository.save(user);
                return true;
            }
            return false;
        }
    */
    public Map<String, String> changeLockStatus(String username, String operation) {
        User user = userRepository.findUserByEmailIgnoreCase(username)
                .orElseThrow(UserNotFoundException::new);

        if (user.getRoles().contains("ROLE_ADMINISTRATOR"))
            throw new AdministratorLockException();

        if (operation.equals("LOCK")) {
            lock(user);
            return Map.of("status", "User " + username + " locked!");
        }
        if (operation.equals("UNLOCK")) {
            unlock(user);
            return Map.of("status", "User " + username + " unlocked!");
        }
        throw new NoSuchOperationException();
    }

    public User findUserByEmail(String email) throws ResponseStatusException {
        return userRepository
                .findUserByEmailIgnoreCase(email)
                .orElseThrow(UserNotFoundException::new);
    }

    /*  @PostMapping("/signup") */
    public UserRepresentation registerUser(User newUser, UserDetails auth) {
        Optional<User> userOptional = userRepository.findUserByEmailIgnoreCase(newUser.getEmail());
        if (userOptional.isPresent())
            throw new UserExistException();

        Optional<UserDetails> object = Optional.ofNullable(auth);
        String author = object.isPresent() ? auth.getUsername().toLowerCase(Locale.ROOT) : "Anonymous";

        events.save(new Logging(Event.CREATE_USER, author, newUser.getEmail().toLowerCase(Locale.ROOT), "/api/auth/signup"));

        return new UserRepresentation(userRepository.save(
                getUserModelWithAddedRoles(userRepository.findAll().isEmpty(), newUser)));
    }

    public User getUserModelWithAddedRoles(boolean isFirstUser, User appUser) {
        String role = isFirstUser ? "ADMINISTRATOR" : "USER";
        appUser.getRoles().add(role);
        appUser.setEmail(appUser.getEmail().toLowerCase(Locale.ROOT));
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setAccountNonLocked(true);
        return appUser;
    }

    public User changePassword(UserDetails auth, NewPassword newPassword) {
        String email = auth.getUsername().toLowerCase(Locale.ROOT);
        Optional<User> appUserOptional = userRepository.findUserByEmailIgnoreCase(email);
        User appUser = appUserOptional.orElseThrow(UserNotFoundException::new);

        if (passwordEncoder.matches(newPassword.getNew_password(), appUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }
        appUser.setPassword(passwordEncoder.encode(newPassword.getNew_password()));
        userRepository.save(appUser);

        events.save(
                new Logging(
                        Event.CHANGE_PASSWORD,
                        email,
                        email,
                        "/api/auth/signup"));

        return appUser;

    }

    public List<UserRepresentation> getAllUsersAndInfo() {
        return userRepository
                .findAll()
                .stream()
                .map(UserRepresentation::new)
                .collect(Collectors.toCollection(LinkedList::new));
    }


    public UserRepresentation updateUserRole(RoleOperation roleOperation, String author) throws ResponseStatusException {

        String operation = roleOperation.getOperation();
        String role = roleOperation.getRole().toUpperCase(Locale.ROOT);

        if (!groups.existsByCode("ROLE_" + role)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        }
        User user = userRepository.findUserByEmailIgnoreCase(roleOperation.getUser())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        Set<String> userRoles = user.getRoles();

        if ("GRANT".equals(operation)) {
            if (userRoles.contains("ADMINISTRATOR") || "ADMINISTRATOR".equals(role)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
            }
            userRoles.add(role);
//            user.modifyRole(operation, groups.get(role));
            events.save(
                    new Logging(Event.GRANT_ROLE,
                            author,
                            String.format("Grant role %s to %s", role, roleOperation.getUser().toLowerCase(Locale.ROOT)),
                            "/api/admin/user/role"));
        } else if ("REMOVE".equals(operation)) {
            if (!userRoles.contains(role)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
            }
            if ("ADMINISTRATOR".equals(role)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
            }
            if (userRoles.size() < 2) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
            }
            userRoles.remove(role);
            events.save(
                    new Logging(
                            Event.REMOVE_ROLE,
                            author,
                            String.format("Remove role %s from %s", role, roleOperation.getUser().toLowerCase(Locale.ROOT)),
                            "/api/admin/user/role"));
        }

        //  user.modifyRole(operation, ROLES.get(role));
        userRepository.save(user);

        return new UserRepresentation(user);
    }


    public Map<String, String> deleteUserByEmail(String email, String author) throws ResponseStatusException {
        User appUser = userRepository.findUserByEmailIgnoreCase(email)
                .orElseThrow(
                        UserNotFoundException::new);

        if (appUser.getRoles().contains("ADMINISTRATOR")) {
            throw new DeleteAdministratorRoleException();
        }
        userRepository.delete(appUser);

        events.save(
                new Logging(
                        Event.DELETE_USER,
                        author,
                        email,
                        "/api/admin/user"));

        return Map.of(
                "user", email,
                "status", "Deleted successfully!"
        );
    }


}
