package com.example.controllers;


import com.example.models.Doctor;
import com.example.models.Patient;
import com.example.models.Preference;
import com.example.repositories.DoctorRepository;
import com.example.repositories.PatientRepository;
import com.example.repositories.PreferenceRepository;
import com.example.service.JwtService;
import com.example.utils.PreferenceRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@Log
@RestController
@RequestMapping("/hospitalplanner")
public class PreferenceController {
    @Autowired
    private final PreferenceRepository preferenceRepository;
    @Autowired
    private final DoctorRepository doctorRepository;
    @Autowired
    private final PatientRepository patientRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtService jwtService;

    @Autowired
    public PreferenceController(PreferenceRepository preferencesRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.preferenceRepository = preferencesRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    /**
     *
     * @return all preferences of the current user
     */
    @GetMapping("/preferences")
    public Set<Preference> getAllPreferences(){
        Patient currentUser = getPatientFromToken();
        if (currentUser != null) {
            return currentUser.getPreferences(); // Presupunând că există o metodă getPreferences() pe obiectul Patient pentru a obține lista de preferințe.
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    /**
     * return all details about a preference with the given id if exists else Not_Found
     * @param id of a preference
     * @return details about a preference
     */
    @GetMapping("/preferences/{id}")
    public ResponseEntity<String> findPreference(@PathVariable("id") Integer id)
    {
        Optional<Preference> preference = preferenceRepository.findById(id);
        if (preference.isPresent()) {
            Preference preferenceObj = preference.get();
            Doctor doctor = preferenceObj.getDoctor();
            Patient patient = preferenceObj.getPatient();
            Patient currentUser=getPatientFromToken();

            if(currentUser!=null && currentUser.getId().equals(patient.getId()))
                return ResponseEntity.ok("Preference ID: " + preferenceObj.getId() +
                    "\nDoctor ID: " + doctor.getId() +
                    "\nDoctor Name: " + doctor.getDoctorName() +
                    "\nPatient ID: " + patient.getId() +
                    "\nPatient Name: " + patient.getPatientName());
            else return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nu s-a gasit nici o preferenta cu acest id");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Nu s-a gasit nici o preferenta cu acest id");
    }

    /**
     * creates a new preference for the current user with the given id of a doctor if the doctor exists
     * @param request id of a doctor
     * @return details about the new created preference
     */
    @PostMapping("/preferences")
    public ResponseEntity<Preference> createPreferences(@RequestBody PreferenceRequest request) {
        //System.out.println(request);
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with id: " + request.getDoctorId()));

        Patient currentUser=getPatientFromToken();

        Preference preference = new Preference();
        preference.setDoctor(doctor);
        preference.setPatient(currentUser);

        return ResponseEntity.ok(preferenceRepository.save(preference));
    }

    /**
     * search for the preference with the given id and delete it from the db
     * @param id of a preference
     * @return success status code(No_Content) or Not_Found
     */
    @DeleteMapping("/preferences/{id}")
    public ResponseEntity<String> deletePreference(@PathVariable("id") Integer id)
    {
        Optional<Preference> preference=preferenceRepository.findById(id);
        if(preference.isPresent())
        {
            Patient currentUser=getPatientFromToken();
            if(currentUser!=null && currentUser.getId().equals(preference.get().getPatient().getId())) {
                preferenceRepository.deleteById(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("Am sters aceasta programare");
            }
            else return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nu s-a gasit nici o programare cu acest id");
        }
        return ResponseEntity.notFound().build();
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
