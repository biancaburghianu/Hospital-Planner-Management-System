package com.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
@EntityListeners(AuditingEntityListener.class)
public class Patient {
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

    @JsonIgnore
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    Set<Preference> preferences;
}
