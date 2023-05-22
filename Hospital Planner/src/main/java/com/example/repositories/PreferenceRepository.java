package com.example.repositories;

import com.example.models.Patient;
import com.example.models.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PreferenceRepository extends JpaRepository<Preference, Integer> {

    @Query("SELECT p FROM Preference p WHERE p.patient.id = :patientId")
    Optional<Preference> findByPatientId(Integer patientId);
}
