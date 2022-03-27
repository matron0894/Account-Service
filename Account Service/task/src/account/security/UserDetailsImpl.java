package account.security;

import account.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(x -> "ROLE_" + x.getCode().toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
//        return Set.of(new SimpleGrantedAuthority("ROLE_USER"));
//
//        if (user.getRoles().isEmpty()) {
//            return Set.of(new SimpleGrantedAuthority("ROLE_USER"));
//        }
//        else {
//            return user.getRoles().stream().map(x -> "ROLE_" + x.getCode().toUpperCase())
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toSet());
//        }
    }


    @Override
    public String getPassword() {
        return  user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
