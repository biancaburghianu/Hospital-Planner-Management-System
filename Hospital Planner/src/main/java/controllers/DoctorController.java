package controllers;

import models.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import respositories.DoctorRepository;

import java.util.List;
@RestController
@RequestMapping("/api/")
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
}