package account.service;

import account.model.Event;
import account.model.Logging;
import account.model.User;
import account.repos.LogRepository;
import account.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoginAttemptService {

    private final UserRepository users;
    private final LogRepository events;

    public static final int MAX_FAILED_ATTEMPTS = 5;

    @Autowired
    public LoginAttemptService(UserRepository users, LogRepository logRepository) {
        this.users = users;
        this.events = logRepository;
    }

    public List<Logging> getAllEvents() {
        return events.findAll();
    }


    public void onSuccess(String email) {
        if (users.existsByEmail(email)) {
            User user = users.findUserByEmailIgnoreCase(email).get();
            if (user.getFailedAttempt() > 0) {
                user.setFailedAttempt(0);
                users.save(user);
            }
        }
    }

    public void onFailure(String email, String path) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!  onFailure !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Optional<User> userOptional = users.findUserByEmailIgnoreCase(email);
        User user = userOptional.orElse(null);

        if (user != null) {
            if (user.isAccountNonLocked()) {
                if (user.getFailedAttempt() < MAX_FAILED_ATTEMPTS - 1) {
                    user.setFailedAttempt(user.getFailedAttempt() + 1);
                    users.save(user);
                    events.save(new Logging(Event.LOGIN_FAILED, email, path, path));
                } else {
                    user.setAccountNonLocked(false);
                    user.setFailedAttempt(0);
                    users.save(user);
                    events.save(new Logging(Event.LOGIN_FAILED, email, path, path));
                    events.save(new Logging(Event.BRUTE_FORCE, email, path, path));
                    events.save(new Logging(Event.LOCK_USER, email, "Lock user " + email, path));
                }
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User account is locked");
            }
        } else {
            events.save(new Logging(Event.LOGIN_FAILED, email, path, path));
        }
    }
}