package com.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
@EntityListeners(AuditingEntityListener.class)
public class Patient implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @Column(name = "email", nullable = false)
    private String patientEmail;

    @Column(name = "phone_number", nullable = false)
    private String patientPhoneNumber;

    @Column(name = "password", nullable = false)
    private String patientPassword;

    @Column(name = "birth_date", nullable = false)
    private String patientBirthDate;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    Set<Preference> preferences;

    @JsonIgnore
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    Set<Appointment> appointments;

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", patientName='" + patientName + '\'' +
                ", patientEmail='" + patientEmail + '\'' +
                ", patientPhoneNumber='" + patientPhoneNumber + '\'' +
                ", patientPassword='" + patientPassword + '\'' +
                ", patientBirthDate='" + patientBirthDate + '\'' +
                ", preferences=" + preferences +
                ", appointments=" + appointments +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return patientPassword;
    }

    @Override
    public String getUsername() {
        return patientEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
