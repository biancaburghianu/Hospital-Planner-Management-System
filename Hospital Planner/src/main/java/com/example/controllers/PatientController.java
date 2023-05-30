package com.example.controllers;

import com.example.models.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.repositories.PatientRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hospitalplanner")
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;


    @GetMapping("/patients")
    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }

    @GetMapping("/patients/{id}")
    public String findPatient(@PathVariable("id") Integer id)
    {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            Patient patientObj = patient.get();

            return "Patient ID: " + patientObj.getId() +
                    "\nPatient Name: " + patientObj.getPatientName() +
                    "\nEmail: " + patientObj.getPatientEmail() +
                    "\nPhone Number: " + patientObj.getPatientPhoneNumber() +
                    "\nBirth Date: " + patientObj.getPatientBirthDate();
        }
        return "Nu exista";
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