package com.example.config;

import com.example.models.Doctor;
import com.example.models.Patient;
import com.example.repositories.DoctorRepository;
import com.example.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final PatientRepository repository;
    private final DoctorRepository doctorRepository;

    /**
     * Creates a user details service bean that loads user details based on the provided username.
     *
     * @return The user details service.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                var patient = repository.findByEmail(username);
                if (patient.isPresent())
                    return patient.get();
                else {
                    var doctor = doctorRepository.findByEmail(username);
                    if (doctor.isPresent())
                        return doctor.get();
                    else throw new UsernameNotFoundException("User not found");
                }
            }
        };
    }

    /**
     * Creates an authentication provider bean that uses the user details service and password encoder.
     *
     * @return The authentication provider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Creates an authentication manager bean that retrieves the authentication manager from the configuration.
     *
     * @param config The authentication configuration.
     * @return The authentication manager.
     * @throws Exception If an exception occurs while retrieving the authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Creates a password encoder bean for encoding and verifying passwords.
     *
     * @return The password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
