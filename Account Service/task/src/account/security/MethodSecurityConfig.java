package account.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true, //enables Spring Security pre/post annotations
        securedEnabled = true, //property determines if the @Secured annotation should be enabled
        jsr250Enabled = true) //allows us to use the @RoleAllowed annotation.
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
}
