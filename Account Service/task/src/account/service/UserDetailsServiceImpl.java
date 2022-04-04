package account.service;

import account.model.User;
import account.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * MyUserDetailsServiceImpl. Содержит методы для бизнес-логики приложения. Этот класс реализует интерфейс
 * UserDetailsService(необходим для Spring Security), в котором нужно переопределить один метод loadUserByUsername().
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    //private LoginAttemptService loggingService;


    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("!!!!!!!!!!!!!!!!!!!! in UserDetailsServiceImpl");
        Optional<User> userOptional = userRepository.findUserByEmailIgnoreCase(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserDetailsImpl(user);
        }
        throw new UsernameNotFoundException("Not found: " + username);
    }


}
