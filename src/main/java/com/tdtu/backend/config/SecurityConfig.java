package com.tdtu.backend.config;

import com.tdtu.backend.model.Role;
import com.tdtu.backend.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/index", "/register", "/login","/rooms","/services",
                                "/services/details/**","/about","/contact",
                                "/rooms/details/**","/css/**","/uploads/**","/confirm-account","/registrationPending",
                                "/js/**", "/img/**","/vendor/**","/fonts/**","/js/**")
                        .permitAll()
                        .requestMatchers("/cart", "/cart/**","/cart/add", "/orders", "/checkout",
                                "/user/change-password","/booking/create","/user/history/**")
                        .hasAuthority("ROLE_USER")
                        .requestMatchers("/admin/**", "/rooms/edit/**","/rooms/update/**", "/rooms/delete/**", "/rooms/create/**")
                        .hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .successHandler(new CustomLoginSuccessHandler())
                )
                .logout(logout -> logout
                        .permitAll()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                );
        return http.build();
    }
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
