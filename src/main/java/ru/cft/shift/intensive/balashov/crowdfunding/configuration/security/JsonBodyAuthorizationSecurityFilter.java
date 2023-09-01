package ru.cft.shift.intensive.balashov.crowdfunding.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JsonBodyAuthorizationSecurityFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    private final RequestMatcher processAuthenticationRequestMatcher;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    public JsonBodyAuthorizationSecurityFilter(AuthenticationManager authenticationManager, Environment env) {
        this.authenticationManager = authenticationManager;
        this.successHandler = new SimpleUrlAuthenticationSuccessHandler(env.getRequiredProperty("api.unauthorized.users.login.success"));
        this.failureHandler = new SimpleUrlAuthenticationFailureHandler(env.getRequiredProperty("api.unauthorized.users.login.failure"));
        this.processAuthenticationRequestMatcher = new AntPathRequestMatcher(env.getRequiredProperty("api.unauthorized.users.login"), HttpMethod.POST.name());
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (this.processAuthenticationRequestMatcher.matches(request)) {
            try {
                Authentication authentication = createFromBody(request);
                authentication = authenticationManager.authenticate(authentication);
                SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
                context.setAuthentication(authentication);
                this.securityContextHolderStrategy.setContext(context); //?????????????????????????????????
                this.securityContextRepository.saveContext(context, request, response);
                this.successHandler.onAuthenticationSuccess(request, response, authentication);
            } catch (AuthenticationServiceException e) {
                SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
                this.securityContextHolderStrategy.clearContext();
                context.setAuthentication(null);
                this.failureHandler.onAuthenticationFailure(request, response, e);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private Authentication createFromBody(HttpServletRequest request) {
        try {
            ObjectNode jsonBodyObjectNode = this.objectMapper.readValue(request.getInputStream(), ObjectNode.class);

            if (jsonBodyObjectNode.has("login") && jsonBodyObjectNode.has("password")) {
                String login = jsonBodyObjectNode.get("login").asText();
                String password = jsonBodyObjectNode.get("password").asText();
                return UsernamePasswordAuthenticationToken.unauthenticated(login, password);
            } else {
                throw new BadCredentialsException("No fields representing username and password");
            }
        } catch (IOException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }
}
