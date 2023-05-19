package com.example.controllers;

import com.example.models.Doctor;
import com.example.models.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.repositories.PatientRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;


    @GetMapping("/patients")
    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }

    @PostMapping("/patients")
    public Patient createPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }
    @DeleteMapping("/patients/{id}")
    public String deletePatients(@PathVariable("id") Integer id)
    {
        Optional<Patient> patient=patientRepository.findById(id);
        if(patient.isPresent())
        {
            patientRepository.deleteById(id);
            return "S-a sters";
        }
        return "Nu s-a sters";
    }
}