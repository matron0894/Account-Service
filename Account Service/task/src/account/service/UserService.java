package account.service;

import account.domain.User;
import account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Optional<User> findUser(String email) {
        return userRepository.findUserByEmailIgnoreCase(email);
    }

    public void addNewUser(User myUser) {
        myUser.setEmail(myUser.getEmail().toLowerCase(Locale.ROOT));
        userRepository.save(myUser);
    }
}