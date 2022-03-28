package account.service;

import account.exception.UserExistException;
import account.model.User;
import account.repos.UserRepository;
import account.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * MyUserDetailsServiceImpl. Содержит методы для бизнес-логики приложения. Этот класс реализует интерфейс
 * UserDetailsService(необходим для Spring Security), в котором нужно переопределить один метод loadUserByUsername().
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmailIgnoreCase(username);
        return user.map(UserDetailsImpl::new)
                .orElseThrow(UserExistException::new);
    }


}
