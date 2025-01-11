package com.example.goodreads.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;  // Wstrzykiwanie zależności

    // Wstrzykiwanie UserDetailsService przez konstruktor
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider getDaoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService); // Wstrzykiwanie usługi
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());  // Użycie BCryptPasswordEncoder
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")  // Tylko dla użytkowników z rolą ADMIN
                .requestMatchers("/user/**").hasRole("USER")    // Tylko dla użytkowników z rolą USER
                .requestMatchers("/**").permitAll()             // Pozwolenie na dostęp do reszty stron
                .and()
                .formLogin()
                .loginPage("/signin")                          // Własna strona logowania
                .loginProcessingUrl("/login")                   // URL, na który wysyłane są dane logowania
                .defaultSuccessUrl("/user/")                    // Strona po udanym logowaniu
                .and()
                .csrf().disable();                               // Wyłączenie CSRF (jeśli aplikacja nie używa sesji)

        return http.build();
    }
}
