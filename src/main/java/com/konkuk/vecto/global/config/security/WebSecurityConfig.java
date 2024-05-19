package com.konkuk.vecto.global.config.security;

import com.konkuk.vecto.global.filter.JwtAuthenticationEntryPoint;
import com.konkuk.vecto.global.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .and()
                .ignoring().requestMatchers(HttpMethod.POST,"/user").requestMatchers(HttpMethod.GET, "/user")
                .requestMatchers("/swagger-ui/**")
                .requestMatchers("/swagger").requestMatchers("/v3/**")
                .requestMatchers("/userId/check").requestMatchers("/login")
                .requestMatchers("/mail")
                .requestMatchers(HttpMethod.GET, "/feed/feedList", "/feed/feeds/search", "/feed/{feedId}", "/feed/{feedId}/comments",
                        "/feed/likes", "/feed")
                .requestMatchers(HttpMethod.GET, "/introduction.html", "/privacy-policy", "/health", "/",
                        "code/*");
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .cors(AbstractHttpConfigurer::disable)

                .formLogin(AbstractHttpConfigurer::disable)

                .httpBasic(AbstractHttpConfigurer::disable)


                .exceptionHandling((exceptionHandlingConfigurer)->
                        exceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint))

                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


       return http.build();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
