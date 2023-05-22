package com.example.repositories;

import com.example.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query("SELECT a.time FROM Appointment a WHERE a.doctor.id = :doctorId")
    List<String> findAppointmentTimesByDoctorId(Integer doctorId);

}