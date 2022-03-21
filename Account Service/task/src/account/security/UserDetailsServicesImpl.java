package account.security;

import account.model.User;
import account.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class UserDetailsServicesImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email " + username + " not found"));
        return
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getName())
                        .username(user.getLastname())
                        .username(user.getEmail().toLowerCase(Locale.ROOT))
                        .password(user.getPassword())
                        .roles("ADMIN")
                        .build();
    }
    public User getUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmailIgnoreCase(email.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + email));

    }
}
