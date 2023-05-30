package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPatient {
    private String patientName;

    private String patientEmail;

    private String patientPhoneNumber;

    private String patientPassword;

    private String patientBirthDate;
}
