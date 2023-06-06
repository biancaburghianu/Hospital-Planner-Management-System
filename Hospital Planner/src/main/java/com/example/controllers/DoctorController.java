package com.example.controllers;

import com.example.models.Doctor;
import com.example.models.Patient;
import com.example.repositories.PatientRepository;
import com.example.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.example.repositories.DoctorRepository;

import java.util.List;
import java.util.Optional;
@Log
@RequestMapping("/hospitalplanner")
@RestController
public class DoctorController {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/doctors")
    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }

    /**
     * using id given return information about a doctor if the doctor exists in db
     * @param id of a doctor
     * @return details about a doctor
     */
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

    /**
     * delete from db current user information-works like delete account
     * @return success message (No_Content) or Not_Found
     */
    @DeleteMapping("/doctors/deleteAccount")
    public ResponseEntity<Void> deleteDoctor()
    {
        Doctor currentUser=getDoctorFromToken();
        if(currentUser!=null)
        {
            Optional<Doctor> doctor=doctorRepository.findById(currentUser.getId());
            if(doctor.isPresent())
            {
                doctorRepository.deleteById(currentUser.getId());
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }
        throw new UsernameNotFoundException("User not found");
    }

    /**
     * using the jwt get the current user if it is a doctor or null if a patient is authenticated
     * @return current doctor or null
     */
    private Doctor getDoctorFromToken() {
        String token = request.getHeader("Authorization");
        String username;
        String jwtToken;
        Optional<Doctor> currentUser = Optional.empty();
        if (token != null && token.startsWith("Bearer")) {
            jwtToken = token.substring(7);
            try {
                username = jwtService.extractUsername(jwtToken);
                Optional<Patient> currentPatient = patientRepository.findByEmail(username);
                if (currentPatient.isPresent()) {
                    // Return null if a patient is authenticated
                    return null;
                } else {
                    // Retrieve the doctor based on username
                    currentUser = doctorRepository.findByEmail(username);
                }
            } catch (IllegalArgumentException e) {
                log.warning("Unable to get token");
            }
        } else {
            log.info("Token does not begin with Bearer String");
        }
        if (currentUser.isPresent())
        {
            return currentUser.get();
        }

        else throw new UsernameNotFoundException("User not found");
    }
}