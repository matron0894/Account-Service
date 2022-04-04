package account.handlers;


import account.service.LoginAttemptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationFailureListener
        implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFailureListener.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        log.info("!!!!!!!!!!!!!!!!!!  login failed login failed login failed login failed login failed");
        String email = (String) e.getAuthentication().getPrincipal();
        String path = request.getRequestURI();
        if (email != null) {
            loginAttemptService.onFailure(email, path);
        }
    }
}