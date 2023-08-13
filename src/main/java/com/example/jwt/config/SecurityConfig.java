package com.example.jwt.config;

import com.example.jwt.config.jwt.JwtAuthenticationFilter;
import com.example.jwt.filter.MyFilter3;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class)
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin().disable()
            .httpBasic().disable()
            .apply(new MyCustomDsl())
            .and()
            .authorizeHttpRequests()
            .antMatchers("/api/v1/user/**").hasAnyRole("USER", "MANAGER", "ADMIN")
            .antMatchers("/api/v1/manager/**").hasAnyRole("MANAGER", "ADMIN")
            .antMatchers("/api/v1/admin/**").hasAnyRole("ADMIN")
            .anyRequest().permitAll()
            .and().build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(
                AuthenticationManager.class);
            http
                .addFilter(corsFilter)
                .addFilter(new JwtAuthenticationFilter(authenticationManager));
        }
    }
}
