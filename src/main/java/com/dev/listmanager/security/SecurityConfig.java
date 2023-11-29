package com.dev.listmanager.security;

import com.dev.listmanager.security.filter.AccessFilter;
import com.dev.listmanager.security.filter.CookieAuthFilter;
import com.dev.listmanager.security.filter.UserPresenceFilter;
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
    private final UserAuthProvider userAuthProvider;
    private final UserAccessProvider userAccessProvider;

    public SecurityConfig(UserAuthProvider userAuthProvider, UserAccessProvider userAccessProvider) {
        this.userAuthProvider = userAuthProvider;
        this.userAccessProvider = userAccessProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .addFilterBefore(new AccessFilter(userAccessProvider), BasicAuthenticationFilter.class)
                .addFilterBefore(new UserPresenceFilter(userAuthProvider), AccessFilter.class)
                .addFilterBefore(new CookieAuthFilter(userAuthProvider), UserPresenceFilter.class).logout()
                .deleteCookies(CookieAuthFilter.COOKIE_NAME).and()
                .authorizeHttpRequests(requests -> requests.requestMatchers(HttpMethod.POST, "/auth/signup", "/auth/login")
                        .permitAll().requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/swagger-ui/index.html").permitAll().anyRequest().authenticated());

        return http.build();
    }

}
