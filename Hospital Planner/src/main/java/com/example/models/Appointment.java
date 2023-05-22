package com.example.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointments")
@EntityListeners(AuditingEntityListener.class)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    Patient patient;

    @Column(name="time")
    private String time;

    @Column(name="description")
    private String description;

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", doctor=" + doctor +
                ", patient=" + patient +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

// id_doctor,id_pacient, time, description
