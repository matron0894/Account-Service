package account.handlers;

import account.model.User;
import account.service.UserDetailsImpl;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RestLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.findUserByEmail(userDetails.getUsername());
    /*    if (user.getFailedAttempt() > 0) {
            userService.resetFailedAttempts(user.getEmail());
        }*/

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
