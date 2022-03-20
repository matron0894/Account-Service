package account.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    UserDetailsServicesImpl userDetailsService;

    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() // manage access
                .mvcMatchers("/api/empl/payment").authenticated()
                .antMatchers(HttpMethod.POST, "/api/signup",  "/actuator/shutdown").permitAll()
                // other matchers
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
//        http
//                .authenticationEntryPoint(restAuthenticationEntryPoint); // Handle auth error
        http
                .csrf().disable()
                .headers()
                .frameOptions()
                .disable(); // for Postman, the H2 console

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("admin")
                .password("{noop}admin")
                .authorities("ROLE_ADMIN");

        auth
                .authenticationProvider(authenticationProvider()); // or  builder.userDetailsService(userDetailsService);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }


}
