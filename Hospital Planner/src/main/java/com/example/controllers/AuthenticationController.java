package com.example.controllers;

import com.example.models.AuthenticationRequest;
import com.example.models.RegisterDoctor;
import com.example.service.AuthenticationService;
import com.example.models.AuthenticationResponse;
import com.example.models.RegisterPatient;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequestMapping("/hospitalplanner")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    @PostMapping("/register/patient")
    public ResponseEntity<AuthenticationResponse> registerPatient(@RequestBody RegisterPatient request)
    {
        return ResponseEntity.ok((service.registerPatient(request)));
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<AuthenticationResponse> registerDoctor(@RequestBody RegisterDoctor request)
    {
        return ResponseEntity.ok((service.registerDoctor(request)));
    }

    @PostMapping("/authenticate/patient")
    public ResponseEntity<AuthenticationResponse> authenticatePatient(@RequestBody AuthenticationRequest request)
    {
        return ResponseEntity.ok((service.authenticatePatient(request)));
    }

    @PostMapping("/authenticate/doctor")
    public ResponseEntity<AuthenticationResponse> authenticateDoctor(@RequestBody AuthenticationRequest request)
    {
        log.info("before auth");
        return ResponseEntity.ok((service.authenticateDoctor(request)));
    }
}
