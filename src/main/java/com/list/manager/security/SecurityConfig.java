package com.list.manager.security;


import com.list.manager.security.filter.CookieAuthFilter;
import com.list.manager.security.filter.UserPresenceFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserAuthEntryPoint userAuthEntryPoint;
    private final UserAuthProvider userAuthProvider;

    public SecurityConfig(UserAuthEntryPoint userAuthEntryPoint, UserAuthProvider userAuthProvider) {
        this.userAuthEntryPoint = userAuthEntryPoint;
        this.userAuthProvider = userAuthProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().authenticationEntryPoint(userAuthEntryPoint)
                .and()
                .addFilterBefore(new UserPresenceFilter(userAuthProvider), BasicAuthenticationFilter.class)
                .addFilterBefore(new CookieAuthFilter(userAuthProvider), UserPresenceFilter.class)
                .csrf().disable()
                .logout().deleteCookies(CookieAuthFilter.COOKIE_NAME)
                .and()
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.POST, "/api/signup").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )
        ;
        return http.build();
    }

}
