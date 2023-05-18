package controllers;

import models.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import respositories.PatientRepository;

import java.util.List;
@RestController
@RequestMapping("/api/")
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
}