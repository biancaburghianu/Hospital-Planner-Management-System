package com.example.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.models.Role.DOCTOR;
import static com.example.models.Role.PATIENT;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/swagger-ui/index.html", "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html")
                .anonymous()
                .requestMatchers("/hospitalplanner/register/patient","/hospitalplanner/register/doctor","/hospitalplanner/authenticate/patient","/hospitalplanner/authenticate/doctor")
                .permitAll()
                .requestMatchers("/hospitalplanner/favoriteDoctorProgram").hasAuthority(PATIENT.name())
                .requestMatchers("/hospitalplanner/doctors").authenticated()
                .requestMatchers("/hospitalplanner/patients").hasAuthority(DOCTOR.name())
                .requestMatchers(HttpMethod.DELETE,"/hospitalplanner/doctors/**").hasAuthority(DOCTOR.name())
                .requestMatchers(HttpMethod.DELETE,"/hospitalplanner/patients/**").hasAuthority(PATIENT.name())
                .requestMatchers(HttpMethod.POST,"/hospitalplanner/appointments").hasAuthority(PATIENT.name())
                .requestMatchers("/hospitalplanner/appointments/**").hasAnyAuthority(PATIENT.name(), DOCTOR.name())
                .requestMatchers(HttpMethod.GET,"/hospitalplanner/doctors/**").hasAnyAuthority(PATIENT.name(),DOCTOR.name())
                .requestMatchers(HttpMethod.GET,"/hospitalplanner/patients/**").hasAuthority(DOCTOR.name())
                .requestMatchers("/hospitalplanner/preferences","/hospitalplanner/preferences/**").hasAuthority(PATIENT.name())

                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
