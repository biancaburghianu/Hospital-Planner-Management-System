package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDoctor {
    private String doctorName;

    private String doctorEmail;

    private String doctorPhoneNumber;

    private String doctorPassword;

    private String doctorOffice;

    private String doctorSpecialisation;
}
