package account.security;

import account.handlers.LimitLoginAuthenticationProvider;
import account.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(
        prePostEnabled = true, //enables Spring Security pre/post annotations
        securedEnabled = true, //property determines if the @Secured annotation should be enabled
        jsr250Enabled = true) //allows us to use the @RoleAllowed annotation.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private LimitLoginAuthenticationProvider authProvider;


    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint) // Handle auth error

                .and()
                .csrf().disable()
                .headers()
                .frameOptions()
                .disable() // for Postman, the H2 console*/

                .and()
                .authorizeRequests() // manage access
                .mvcMatchers("/h2/**", "/h2-console/**", "/api/auth/signup", "/actuator/shutdown").permitAll()
                .mvcMatchers("/api/empl/payment").hasAnyRole("ACCOUNTANT", "USER")
                .mvcMatchers("/api/acct/payments").hasRole("ACCOUNTANT")
                .mvcMatchers("/api/admin/**").hasRole("ADMINISTRATOR")
                .anyRequest()
                .authenticated()
                // other matchers

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no session

                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      /*  auth
                .inMemoryAuthentication()
                .withUser("admin")
                .password("{bcrypt}administrator")
                .authorities("ROLE_ADMINISTRATOR");*/

        //  auth.authenticationProvider(new LimitLoginAuthenticationProvider(userDetailsService, passwordEncoder())); // or  builder.userDetailsService(userDetailsService);
//        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(getProvider());

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider getProvider() {
        LimitLoginAuthenticationProvider authProvider = new LimitLoginAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}

