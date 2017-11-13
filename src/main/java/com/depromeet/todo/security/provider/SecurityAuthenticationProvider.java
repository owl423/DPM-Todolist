package com.depromeet.todo.security.provider;

import com.depromeet.todo.model.User;
import com.depromeet.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public SecurityAuthenticationProvider(UserService userService, BCryptPasswordEncoder encoder) {

        this.userService = userService;
        this.encoder = encoder;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        User user = (User) userService.loadUserByUsername(username);

        if (!encoder.matches(password, user.getHash())) {
            throw new BadCredentialsException("");
        }

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        result.setDetails(user);

        return result;
    }

    public boolean supports(Class<?> type) {

        return type.equals(UsernamePasswordAuthenticationToken.class);
    }
}