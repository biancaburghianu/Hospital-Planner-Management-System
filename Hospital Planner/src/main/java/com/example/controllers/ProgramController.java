package com.example.controllers;

import com.example.models.Appointment;
import com.example.models.Doctor;
import com.example.models.Patient;
import com.example.models.Preference;
import com.example.repositories.AppointmentRepository;
import com.example.repositories.DoctorRepository;
import com.example.repositories.PatientRepository;
import com.example.repositories.PreferenceRepository;
import com.example.service.JwtService;
import com.example.utils.HospitalProgram;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@Log
@RestController
@RequestMapping("/hospitalplanner")
public class ProgramController {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private HttpServletRequest request;

    /**
     * if the user does not have a preference he gets a message about that
     * if the user has a preference he receives as response with the availability of his program
     * @return working program of the favorite doctor
     */
    @GetMapping("/favoriteDoctorProgram")
    public String getFavoriteDoctorProgram() {
        Patient currentUser = getPatientFromToken();
        if(currentUser!=null)
        {
            Integer patientId=currentUser.getId();
            Optional<Preference> preferenceOptional = preferenceRepository.findByPatientId(patientId);
            if (preferenceOptional.isPresent()) {
                Preference preference = preferenceOptional.get();
                Doctor favoriteDoctor = preference.getDoctor();

                List<Appointment> appointments = appointmentRepository.findAppointmentsByDoctor(favoriteDoctor.getId());

                List<String> appointmentTimes = new ArrayList<>();
                for (Appointment appointment : appointments) {
                    appointmentTimes.add(appointment.getTime());
                }

                HospitalProgram hospitalProgram = new HospitalProgram();
                List<String> availableTimes = hospitalProgram.getAvailableAppointments(appointmentTimes);

                StringBuilder message = new StringBuilder();
                message.append("Doctorul tau preferat ")
                        .append(favoriteDoctor.getDoctorName())
                        .append(" este liber la urmatoarele ore:\n");
                for (String time : availableTimes) {
                    message.append(time).append("\n");
                }
                return message.toString();
            } else {
                return "Nu exista o preferinta";
            }
        }
        else
            throw new UsernameNotFoundException("User not found");

    }

    /**
     * using a given id check if the doctor exists
     * check when the doctor has other appointments and return only the program when he is available
     * @param doctorId id of a doctor from db
     * @return the working program of a doctor
     */
    @GetMapping("/doctorProgram/{doctor_id}")
    public String getDoctorProgram(@PathVariable("doctor_id") Integer doctorId) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDoctor(doctorId);

        Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);
        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();

            List<String> appointmentTimes = new ArrayList<>();
            for (Appointment appointment : appointments) {
                appointmentTimes.add(appointment.getTime());
            }

            HospitalProgram hospitalProgram = new HospitalProgram();
            List<String> availableTimes = hospitalProgram.getAvailableAppointments(appointmentTimes);

            StringBuilder message = new StringBuilder();
            message.append("Doctorul ")
                    .append(doctor.getDoctorName())
                    .append(" este liber la urmatoarele ore:\n");
            for (String time : availableTimes) {
                message.append(time);
                message.append("\n");
            }
            return message.toString();
        } else {
            return "Doctorul cu id-ul specificat nu exista";
        }
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

