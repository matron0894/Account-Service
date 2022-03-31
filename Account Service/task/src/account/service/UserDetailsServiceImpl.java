package account.service;

import account.exception.UserExistException;
import account.model.User;
import account.model.UserDetailsDao;
import account.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

/**
 * MyUserDetailsServiceImpl. Содержит методы для бизнес-логики приложения. Этот класс реализует интерфейс
 * UserDetailsService(необходим для Spring Security), в котором нужно переопределить один метод loadUserByUsername().
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserDetailsDao {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("!!!!!!!!!!!!!!!!!!!! in UserDetailsServiceImpl");
        Optional<User> user = userRepository.findUserByEmailIgnoreCase(username);
        return user.map(UserDetailsImpl::new)
                .orElseThrow(UserExistException::new);
    }


    public static final int MAX_FAILED_ATTEMPTS = 5;
    //private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000; // 24 hour, duration of the lock time in milliseconds


    @Override
    public void updateFailAttempts(String username) {
        Optional<User> optional = userRepository.findUserByEmailIgnoreCase(username);
        if (optional.isPresent()) {
            User loginUser = optional.get();
            if (loginUser.isAccountNonLocked()) {
                if (loginUser.getFailedAttempt() < MAX_FAILED_ATTEMPTS - 1) {
                    increaseFailedAttempts(loginUser);
                } else {
                    loginUser.setAccountNonLocked(false);
                    loginUser.setLockTime(LocalDate.now());
                    userRepository.save(loginUser);
                    throw new LockedException("Your account has been locked due to 3 failed attempts."
                            + " It will be unlocked after 24 hours.");
                }
            }
        }

    }

    @Override
    public void resetFailAttempts(String username) {
        int res = userRepository.updateFailedAttemptsForUser(0, username);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!  resetFailAttempts: " + res);
    }


    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        userRepository.updateFailedAttemptsForUser(newFailAttempts, user.getEmail());
    }

}
