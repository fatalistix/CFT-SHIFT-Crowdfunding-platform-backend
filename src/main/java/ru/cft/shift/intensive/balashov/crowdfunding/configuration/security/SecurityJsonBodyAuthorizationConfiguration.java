package ru.cft.shift.intensive.balashov.crowdfunding.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@PropertySource("classpath:/api.properties")
public class SecurityJsonBodyAuthorizationConfiguration {
    private final Environment env;

    @Autowired
    public SecurityJsonBodyAuthorizationConfiguration(Environment env) {
        this.env = env;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
//        http.authorizeHttpRequests(requests -> requests.requestMatchers("/users/**").permitAll()
//                .requestMatchers("/balance").authenticated()
//                .requestMatchers("/login").permitAll()
//                .requestMatchers("/user/security").authenticated()
//                .requestMatchers("/projects").permitAll()
//                .requestMatchers("/projects/*/donate").authenticated()
//                .requestMatchers("/projects/*").hasRole(UserRole.ADMIN.getRoleStringValue()));
        http.authorizeHttpRequests(requests -> requests.anyRequest().permitAll());
        http.cors(cfg -> cfg.configurationSource(
                request -> new CorsConfiguration().applyPermitDefaultValues())
        );
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(new JsonBodyAuthorizationSecurityFilter(authenticationManager, env), UsernamePasswordAuthenticationFilter.class);
        http.logout(configurer -> configurer.logoutSuccessUrl(env.getRequiredProperty("api.user.users.logout.success")));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //?????????????????????????????????????????????????????????
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) throws Exception {
        return authenticationProvider::authenticate;
    }
}
