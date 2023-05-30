package com.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctors")
@EntityListeners(AuditingEntityListener.class)
public class Doctor implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "doctor_name", nullable = false)
    private String doctorName;

    @Column(name = "email", nullable = false)
    private String doctorEmail;

    @Column(name = "phone_number", nullable = false)
    private String doctorPhoneNumber;

    @Column(name = "password", nullable = false)
    private String doctorPassword;

    @Column(name = "office", nullable = false)
    private String doctorOffice;

    @Column(name = "specialisation", nullable = false)
    private String doctorSpecialisation;
    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    Set<Preference> preferences;

    @JsonIgnore
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    Set<Appointment> appointments;

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", doctorName='" + doctorName + '\'' +
                ", doctorEmail='" + doctorEmail + '\'' +
                ", doctorPhoneNumber='" + doctorPhoneNumber + '\'' +
                ", doctorPassword='" + doctorPassword + '\'' +
                ", doctorOffice='" + doctorOffice + '\'' +
                ", doctorSpecialisation='" + doctorSpecialisation + '\'' +
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
        return doctorPassword;
    }

    @Override
    public String getUsername() {
        return doctorEmail;
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
