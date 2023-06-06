package com.example.service;

import com.example.models.*;
import com.example.repositories.DoctorRepository;
import com.example.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new patient with the provided registration details.
     *
     * @param request The registration details of the patient.
     * @return An AuthenticationResponse containing the token for the registered patient.
     */
    public AuthenticationResponse registerPatient(RegisterPatient request) {
        var patient= Patient.builder()
                .patientName(request.getPatientName())
                .patientEmail(request.getPatientEmail())
                .patientPhoneNumber(request.getPatientPhoneNumber())
                .patientPassword(passwordEncoder.encode(request.getPatientPassword()))
                .patientBirthDate(request.getPatientBirthDate())
                .role(Role.PATIENT)
                .build();
        patientRepository.save(patient);
        var jwtToken=jwtService.generateToken(patient);
        log.info(jwtToken);
        //Return the token in an AuthenticationResponse
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Registers a new doctor with the provided registration details.
     *
     * @param request The registration details of the doctor.
     * @return An AuthenticationResponse containing the token for the registered doctor.
     */
    public AuthenticationResponse registerDoctor(RegisterDoctor request) {
        var doctor= Doctor.builder()
                .doctorName(request.getDoctorName())
                .doctorEmail(request.getDoctorEmail())
                .doctorPhoneNumber(request.getDoctorPhoneNumber())
                .doctorPassword(passwordEncoder.encode(request.getDoctorPassword()))
                .doctorOffice(request.getDoctorOffice())
                .doctorSpecialisation(request.getDoctorSpecialisation())
                .role(Role.DOCTOR)
                .build();
        doctorRepository.save(doctor);
        var jwtToken=jwtService.generateToken(doctor);
        log.info(jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Authenticates a patient with the provided authentication request.
     *
     * @param request The authentication request containing the patient's email and password.
     * @return An AuthenticationResponse containing the token for the authenticated patient.
     */
    public AuthenticationResponse authenticatePatient(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var patient=patientRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken=jwtService.generateToken(patient);
        log.info(jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Authenticates a doctor with the provided authentication request.
     *
     * @param request The authentication request containing the doctor's email and password.
     * @return An AuthenticationResponse containing the token for the authenticated doctor.
     */
    public AuthenticationResponse authenticateDoctor(AuthenticationRequest request) {
        log.info(request.getEmail());
        log.info(request.getPassword());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        log.info("here");
        var doctor=doctorRepository.findByEmail(request.getEmail())
                .orElseThrow();
        log.info("found");
        var jwtToken=jwtService.generateToken(doctor);
        log.info(jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
