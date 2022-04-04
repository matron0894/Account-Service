package account.service;

import account.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserDetailsImpl implements UserDetails {

    private final String username;
    private final String password;
    private final boolean isAccountNonLocked;
    private final Set<GrantedAuthority> authorityList;

    public UserDetailsImpl(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.isAccountNonLocked = user.isAccountNonLocked();
        Set<GrantedAuthority> userRoles = new HashSet<>();
        Set<String> roles = user.getRoles();
        for (String r : roles) {
            userRoles.add(new SimpleGrantedAuthority("ROLE_" + r));
        }
        this.authorityList = userRoles;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
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
