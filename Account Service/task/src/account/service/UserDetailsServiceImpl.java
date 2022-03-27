package account.service;

import account.exception.*;
import account.model.Group;
import account.model.User;
import account.repos.GroupRepository;
import account.repos.UserRepository;
import account.security.UserDetailsImpl;
import account.view.ChangeUserRoleRepresentation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * MyUserDetailsServiceImpl. Содержит методы для бизнес-логики приложения. Этот класс реализует интерфейс
 * UserDetailsService(необходим для Spring Security), в котором нужно переопределить один метод loadUserByUsername().
 */

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;


    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email " + username + " not found"));
        return new UserDetailsImpl(user);
    }

    public User registerUser(User user) {
        Optional<User> optionalUser = findUserByEmail(user.getEmail());
        if (optionalUser.isPresent()) throw new UserExistException();

        user.setEmail(user.getEmail().toLowerCase(Locale.ROOT));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userRepository.count() == 0) {
            Group admin_role = groupRepository.findByCode("ADMINISTRATOR")
                    .orElseThrow(GroupNotFoundException::new);
            user.getRoles().add(admin_role);
        } else {
            Group user_role = groupRepository.findByCode("USER")
                    .orElseThrow(GroupNotFoundException::new);
            user.getRoles().add(user_role);
//            user.setRoles(Set.of(user_role));
        }
        userRepository.save(user);
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email);
    }

    public User updateUserRole(ChangeUserRoleRepresentation roleRepresentation) {
        User user = userRepository.findUserByEmailIgnoreCase(roleRepresentation.getUser())
                .orElseThrow(UserNotFoundException::new);
        Group role = groupRepository.findByCode(roleRepresentation.getRole())
                .orElseThrow(GroupNotFoundException::new);

        switch (roleRepresentation.getOperation()) {
            case "GRANT": {
                user.getRoles().add(role);
//                user.setRoles(user.getRoles().stream().sorted());
                if (user.getRoles().contains(groupRepository.findByCode("ADMINISTRATOR").get()) &&
                        user.getRoles().contains(groupRepository.findByCode("ACCOUNTANT").get()) ||
                        user.getRoles().contains(groupRepository.findByCode("ADMINISTRATOR").get()) &&
                                user.getRoles().contains(groupRepository.findByCode("USER").get()))
                    throw new CombinedAdminBusinessRolesException();
                break;
            }
            case "REMOVE": {
                if (!user.getRoles().contains(role)) {
                    throw new NoSuchRoleForUserException();
                }
                if (role.getCode().equals("ADMINISTRATOR")) {
                    throw new RemoveAdminException();
                }

                if (user.getRoles().size() <= 1) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
                }
                user.getRoles().remove(role);
                break;
            }
            default:
                throw new NoSuchOperationException();
        }
        return userRepository.save(user);
    }

    public Map<String, String> changePassword(String email, String newPassword) {
        Optional<User> user = userRepository.findUserByEmailIgnoreCase(email);
        User chUser = user.orElseThrow(EmailNotFoundException::new);
        if (passwordEncoder.matches(newPassword, chUser.getPassword())) {
            throw new ErrorChangePasswordException();
        }
        chUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(chUser);
        return Map.of(
                "status", "The password has been updated successfully",
                "email", chUser.getEmail());
    }

    public Map<String, String> deleteUser(String email) {
        User user = userRepository.findUserByEmailIgnoreCase(email)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
        return Map.of(
                "user", "email",
                "status", "Deleted successfully!"
        );
    }

}
