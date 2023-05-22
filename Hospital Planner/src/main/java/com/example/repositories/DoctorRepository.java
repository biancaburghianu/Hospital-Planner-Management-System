package com.example.repositories;

import com.example.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    @Query("SELECT d.doctorName FROM Doctor d WHERE d.id = :doctorId")
    String findDoctorName(Integer doctorId);
}