package com.complaint.cms.config;

import com.complaint.cms.model.Student;
import com.complaint.cms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;

@Configuration
public class SecurityConfig {

    @Autowired
    private StudentRepository studentRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/images/**", "/css/**", "/js/**", "/webjars/**").permitAll()
                .requestMatchers("/", "/login", "/register", "/complaint",
                                 "/submitComplaint", "/track",
                                 "/forgot-password", "/verify-otp", "/reset-password").permitAll()
                .requestMatchers("/student-dashboard", "/change-password").hasRole("STUDENT")
                .requestMatchers("/dashboard", "/hod", "/search", "/updateStatus", "/delete/**").hasRole("HOD")
                .requestMatchers("/principal", "/principalUpdate", "/principalDelete/**", "/principalSearch").hasRole("PRINCIPAL")
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(roleBasedSuccessHandler())
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
            )

            .userDetailsService(combinedUserDetailsService());

        return http.build();
    }



    // Checks HOD/Principal first, then DB for students
    @Bean
    public UserDetailsService combinedUserDetailsService() {
        return username -> {
            if (username.equals("hod")) {
                return User.builder()
                        .username("hod")
                        .password(passwordEncoder().encode("123"))
                        .roles("HOD")
                        .build();
            }
            if (username.equals("principal")) {
                return User.builder()
                        .username("principal")
                        .password(passwordEncoder().encode("123"))
                        .roles("PRINCIPAL")
                        .build();
            }
            // DB lookup for registered students
            Student student = studentRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
            return User.builder()
                    .username(student.getUsername())
                    .password(student.getPassword())
                    .roles("STUDENT")
                    .build();
        };
    }

    @Bean
    public AuthenticationSuccessHandler roleBasedSuccessHandler() {
        return (HttpServletRequest request,
                HttpServletResponse response,
                Authentication authentication) -> {

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String redirectUrl = "/";

            for (GrantedAuthority authority : authorities) {
                String role = authority.getAuthority();
                if (role.equals("ROLE_STUDENT")) {
                    redirectUrl = "/student-dashboard";
                    break;
                } else if (role.equals("ROLE_HOD")) {
                    redirectUrl = "/dashboard";
                    break;
                } else if (role.equals("ROLE_PRINCIPAL")) {
                    redirectUrl = "/principal";
                    break;
                }
            }
            response.sendRedirect(redirectUrl);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
