package account.handlers;

import account.repos.UserRepository;
import account.service.UserDetailsImpl;
import account.service.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LimitLoginAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsServiceImpl userDetailsServiceImpl;
    private PasswordEncoder passwordEncoder;




    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        UserDetailsImpl user = userDetailsServiceImpl.loadUserByUsername(username);

        if (user == null || !user.getUsername().equalsIgnoreCase(username)) {
            throw new BadCredentialsException("Username not found!");
        }
        boolean isEqualsPass = passwordEncoder.matches(password, user.getPassword());
        if (!isEqualsPass) {
            System.out.println("!!!!!!!!!!!!!!!    Wrong password.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            userDetailsServiceImpl.updateFailAttempts(username);
            throw new BadCredentialsException("Wrong password.");
        }

        userDetailsServiceImpl.resetFailAttempts(username);

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    public void setUserDetailsService(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


}
