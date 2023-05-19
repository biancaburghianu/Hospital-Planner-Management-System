package com.example.repositories;

import com.example.models.Patient;
import com.example.models.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<Preference, Integer> {
}
