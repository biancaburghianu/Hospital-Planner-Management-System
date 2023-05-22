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
@Table(name = "doctors")
@EntityListeners(AuditingEntityListener.class)
public class Doctor {
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
}
