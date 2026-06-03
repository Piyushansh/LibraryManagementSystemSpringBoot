package com.librarymanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * =====================================================
 * SecurityConfig - Spring Security Configuration
 * =====================================================
 * This class defines:
 *   1. Which URLs are public vs protected
 *   2. Which roles can access which URLs
 *   3. Login page URL
 *   4. Logout behavior
 *   5. Password encoding method
 *
 * URL Access Rules:
 *   /register, /login → Public (anyone can access)
 *   /admin/**         → Only ADMIN role
 *   /user/**          → Only USER role
 *   /dashboard        → Both ADMIN and USER
 *   /books/**         → Both ADMIN and USER (view)
 * =====================================================
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * PasswordEncoder Bean.
     * BCrypt is a strong hashing algorithm.
     * When user registers → password is encoded.
     * When user logs in → entered password is compared with hash.
     * You CANNOT reverse BCrypt (one-way hash).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * SecurityFilterChain defines all security rules.
     * Every HTTP request passes through this filter.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // --- URL Authorization Rules ---
            .authorizeHttpRequests(auth -> auth
                // Public URLs (no login needed)
                .requestMatchers("/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()

                // Admin-only URLs
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // User-only URLs
                .requestMatchers("/user/**").hasRole("USER")

                // Dashboard, books, issues accessible to both roles
                .requestMatchers("/dashboard", "/books/**", "/issues/**").hasAnyRole("ADMIN", "USER")

                // All other URLs require login (any role)
                .anyRequest().authenticated()
            )

            // --- Login Configuration ---
            .formLogin(form -> form
                .loginPage("/login")                   // our custom login page
                .loginProcessingUrl("/login")          // form submits to this URL
                .usernameParameter("email")            // use "email" field as username
                .passwordParameter("password")
                .defaultSuccessUrl("/dashboard", true) // after login, go to dashboard
                .failureUrl("/login?error=true")       // if login fails, show error
                .permitAll()
            )

            // --- Logout Configuration ---
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")  // after logout, go to login
                .deleteCookies("JSESSIONID")             // clear session cookie
                .invalidateHttpSession(true)
                .permitAll()
            );

        return http.build();
    }

    /**
     * Connects Spring Security to our CustomUserDetailsService.
     * This tells Spring: "use our service to find users + BCrypt to check passwords"
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }
}
