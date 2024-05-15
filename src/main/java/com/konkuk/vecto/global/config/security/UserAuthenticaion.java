package com.konkuk.vecto.global.config.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;

@Getter
public class UserAuthenticaion extends AbstractAuthenticationToken {

    private final String userId;
    public UserAuthenticaion(User user) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

}
