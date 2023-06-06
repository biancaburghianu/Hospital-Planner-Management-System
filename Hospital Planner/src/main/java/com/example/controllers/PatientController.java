package com.example.controllers;

import com.example.models.Doctor;
import com.example.models.Patient;
import com.example.repositories.DoctorRepository;
import com.example.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.example.repositories.PatientRepository;

import java.util.List;
import java.util.Optional;
@Log
@RestController
@RequestMapping("/hospitalplanner")
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HttpServletRequest request;


    /**
     * details about all patients in db
     * @return all patients
     */
    @GetMapping("/patients")
    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }

    /**
     * using the name given as a path variable verify if exists in db and return all details about that patient
     * @param name patient name
     * @return patient with the name specified
     */

    @GetMapping("/patients/{name}")
    public ResponseEntity<String> findPatient(@PathVariable("name") String name)
    {
        Optional<Patient> patient = patientRepository.findByName(name);
        if (patient.isPresent()) {
            Patient patientObj = patient.get();

            return ResponseEntity.ok("Patient ID: " + patientObj.getId() +
                    "\nPatient Name: " + patientObj.getPatientName() +
                    "\nEmail: " + patientObj.getPatientEmail() +
                    "\nPhone Number: " + patientObj.getPatientPhoneNumber() +
                    "\nBirth Date: " + patientObj.getPatientBirthDate());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * using jwt return all details from db of the current user
     * @return all details about the current user
     */
    @GetMapping("/patients/personalDetails")
    public ResponseEntity<String> findDetails()
    {
        Patient currentUser=getPatientFromToken();
        if (currentUser!=null) {

            return ResponseEntity.ok("Patient ID: " + currentUser.getId() +
                    "\nPatient Name: " + currentUser.getPatientName() +
                    "\nEmail: " + currentUser.getPatientEmail() +
                    "\nPhone Number: " + currentUser.getPatientPhoneNumber() +
                    "\nBirth Date: " + currentUser.getPatientBirthDate());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * delete from db current user information-works like delete account
     * @return success message (No_Content) or Not_Found
     */
    @DeleteMapping("/patients/deleteAccount")
    public ResponseEntity<Void> deletePatients()
    {
        Patient currentUser = getPatientFromToken();
        if(currentUser!=null)
        {
            Optional<Patient> patient=patientRepository.findById(currentUser.getId());
            if(patient.isPresent())
            {
                patientRepository.deleteById(currentUser.getId());
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }
        else throw new UsernameNotFoundException("User not found");
    }

    /**
     * using the jwt get the current user if it is a patient or null if a doctor is authenticated
     * @return current patient or null
     */
    private Patient getPatientFromToken() {
        String token = request.getHeader("Authorization");
        String username;
        String jwtToken;
        Optional<Patient> currentUser = Optional.empty();
        if (token != null && token.startsWith("Bearer")) {
            jwtToken = token.substring(7);
            try {
                username = jwtService.extractUsername(jwtToken);
                Optional<Doctor> currentDoctor = doctorRepository.findByEmail(username);
                if (currentDoctor.isPresent()) {
                    // Return null if a patient is authenticated
                    return null;
                } else {
                    // Retrieve the doctor based on username
                    currentUser = patientRepository.findByEmail(username);
                }
            } catch (IllegalArgumentException e) {
                log.warning("Unable to get token");
            }
        } else {
            log.info("Token does not begin with Bearer String");
        }
        if (currentUser.isPresent())
            return currentUser.get();
        else throw new UsernameNotFoundException("User not found");
    }
}