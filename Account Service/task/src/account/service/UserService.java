package account.service;

import account.model.User;
import account.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public Optional<User> findUser(String email) {
        return userRepository.findUserByEmailIgnoreCase(email);
    }

    public void addNewUser(User myUser) {
        myUser.setEmail(myUser.getEmail().toLowerCase(Locale.ROOT));
        myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
        userRepository.save(myUser);
    }

    public boolean changePassword(User user, String new_password) {
        if (!passwordEncoder.matches(new_password, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(new_password));
            userRepository.save(user);
            return true;
        }
        return false;
    }
}