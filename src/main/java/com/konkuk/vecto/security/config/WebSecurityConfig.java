package com.konkuk.vecto.security.config;

import com.konkuk.vecto.security.config.filter.JwtAuthenticationFilter;
import com.konkuk.vecto.security.service.UserService;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 환경 설정을 구성하기 위한 클래스입니다.
 * 웹 서비스가 로드 될때 Spring Container 의해 관리가 되는 클래스이며 사용자에 대한 ‘인증’과 ‘인가’에 대한 구성을 Bean 메서드로 주입을 한다.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    /**
     * 1. 정적 자원(Resource)에 대해서 인증된 사용자가  정적 자원의 접근에 대해 ‘인가’에 대한 설정을 담당하는 메서드이다.
     *
     * @return WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 자원에 대해서 Security를 적용하지 않음으로 설정
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
            .and()
            .ignoring().requestMatchers(HttpMethod.POST,"/user").requestMatchers(HttpMethod.GET, "/user")
                .requestMatchers("/swagger-ui/**")
                .requestMatchers("/swagger").requestMatchers("/v3/**")
                .requestMatchers("/userId/check").requestMatchers("/login")
                .requestMatchers("/mail")
                .requestMatchers(HttpMethod.GET, "/feed/feedList", "/feed/feeds/search", "/feed/{feedId}", "/feed/{feedId}/comments",
                        "/feed/likes", "/feed")
                .requestMatchers(HttpMethod.GET, "", "/", "/introduction.html", "/privacy-policy");
    }

    /**
     * 2. HTTP에 대해서 ‘인증’과 ‘인가’를 담당하는 메서드이며 필터를 통해 인증 방식과 인증 절차에 대해서 등록하며 설정을 담당하는 메서드이다.
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("[+] WebSecurityConfig Start !!! ");

        http
                // [STEP1] 서버에 인증정보를 저장하지 않기에 csrf를 사용하지 않는다.
                .csrf(AbstractHttpConfigurer::disable)


                // [STEP2] 토큰을 활용하는 경우 모든 요청에 대해 '인가'에 대해서 적용
                .authorizeHttpRequests(authorizeRequest -> authorizeRequest
                        .anyRequest().permitAll())


                // [STEP3] Session 기반의 인증기반을 사용하지 않고 JWT를 이용하여서 인증
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // [STEP4] form 기반의 로그인에 대해 비 활성화하며 커스텀으로 구성한 필터를 사용한다.
                .formLogin(AbstractHttpConfigurer::disable)

                .httpBasic(AbstractHttpConfigurer::disable)

                // [STEP5] Spring Security JWT Filter Load
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);



        // [STEP7] 최종 구성한 값을 사용함.
        return http.build();
    }


    /**
     * 3. JWT 토큰을 통하여서 사용자를 인증합니다.
     *
     * @return JwtAuthorizationFilter
     */
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(userDetailsService);
    }


}
