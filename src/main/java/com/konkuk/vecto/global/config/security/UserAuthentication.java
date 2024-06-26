package com.konkuk.vecto.global.config.security;

import com.konkuk.vecto.user.domain.User;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.security.auth.Subject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class UserAuthentication extends AbstractAuthenticationToken {

    private final String userId;
    public UserAuthentication(User user) {
        super(getAuthorities(user));
        this.userId=user.getUserId();
    }

    private static List<GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoleType().name()));
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

}
