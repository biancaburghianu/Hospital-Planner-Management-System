package com.example.controllers;

import com.example.models.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.repositories.DoctorRepository;

import java.util.List;
import java.util.Optional;
@RequestMapping("/hospitalplanner")
@RestController
public class DoctorController {
    @Autowired
    private DoctorRepository doctorRepository;
    @GetMapping("/doctors")
    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }
    @GetMapping("/doctors/{id}")
    public ResponseEntity<String> findDoctor(@PathVariable("id") Integer id)
    {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            Doctor doctorObj = doctor.get();

            return ResponseEntity.ok("Doctor ID: " + doctorObj.getId() +
                    "\nDoctor Name: " + doctorObj.getDoctorName() +
                    "\nEmail: " + doctorObj.getDoctorEmail() +
                    "\nPhone Number: " + doctorObj.getDoctorPhoneNumber() +
                    "\nOffice: " + doctorObj.getDoctorOffice() +
                    "\nSpecialization: " + doctorObj.getDoctorSpecialisation());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable("id") Integer id)
    {
        Optional<Doctor> doctor=doctorRepository.findById(id);
        if(doctor.isPresent())
        {
            doctorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}