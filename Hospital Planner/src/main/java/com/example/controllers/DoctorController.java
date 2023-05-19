package com.example.controllers;

import com.example.models.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.repositories.DoctorRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class DoctorController {
    @Autowired
    private DoctorRepository doctorRepository;
    @GetMapping("/doctors")
    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }
    @PostMapping("/doctors")
    public Doctor createDoctor(@RequestBody Doctor patient) {
        return doctorRepository.save(patient);
    }
    @DeleteMapping("/doctors/{id}")
    public String deleteDoctor(@PathVariable("id") Integer id)
    {
        Optional<Doctor> doctor=doctorRepository.findById(id);
        if(doctor.isPresent())
        {
            doctorRepository.deleteById(id);
            return "S-a sters";
        }
        return "Nu s-a sters";
    }
}