package com.example.controllers;

import com.example.models.Doctor;
import com.example.models.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.repositories.PatientRepository;

import java.util.List;

@RestController
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;


    @GetMapping("/patients")
    public List<Patient> getAllDoctors(){
        return patientRepository.findAll();
    }

    @PostMapping("/patients")
    public Patient createPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }
}