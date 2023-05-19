package com.example.controllers;

import com.example.models.Doctor;
import com.example.models.Patient;
import com.example.models.Preference;
import com.example.repositories.DoctorRepository;
import com.example.repositories.PatientRepository;
import com.example.repositories.PreferenceRepository;
import com.example.utils.PreferenceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PreferenceController {
    @Autowired
    private final PreferenceRepository preferenceRepository;
    @Autowired
    private final DoctorRepository doctorRepository;
    @Autowired
    private final PatientRepository patientRepository;

    @Autowired
    public PreferenceController(PreferenceRepository preferencesRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.preferenceRepository = preferencesRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @GetMapping("/preferences")
    public List<Preference> getAllPreferences(){
        return preferenceRepository.findAll();
    }

    @PostMapping("/preferences")
    public Preference createPreferences(@RequestBody PreferenceRequest request) {
        System.out.println(request);
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with id: " + request.getDoctorId()));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with id: " + request.getPatientId()));

        Preference preference = new Preference();
        preference.setDoctor(doctor);
        preference.setPatient(patient);

        return preferenceRepository.save(preference);
    }
    @DeleteMapping("/preferences/{id}")
    public String deletePreference(@PathVariable("id") Integer id)
    {
        Optional<Preference> preference=preferenceRepository.findById(id);
        if(preference.isPresent())
        {
            preferenceRepository.deleteById(id);
            return "S-a sters";
        }
        return "Nu s-a sters";
    }
}
