package com.example.utils;

public class PreferenceRequest {
    Integer patientId;
    Integer doctorId;

    public PreferenceRequest(Integer patientId, Integer doctorId) {
        this.patientId = patientId;
        this.doctorId = doctorId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    @Override
    public String toString() {
        return "PreferenceRequest{" +
                "patientId=" + patientId +
                ", doctorId=" + doctorId +
                '}';
    }
}
