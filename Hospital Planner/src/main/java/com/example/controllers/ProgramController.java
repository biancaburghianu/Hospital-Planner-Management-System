package com.example.controllers;

import com.example.models.Appointment;
import com.example.models.Doctor;
import com.example.models.Preference;
import com.example.repositories.AppointmentRepository;
import com.example.repositories.DoctorRepository;
import com.example.repositories.PreferenceRepository;
import com.example.utils.HospitalProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ProgramController {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/favoriteDoctorProgram")
    public String getFavoriteDoctorProgram(@RequestParam("patientId") Integer patientId) {
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
}

