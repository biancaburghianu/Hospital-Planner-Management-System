package com.example.utils;

public class AppointmentRequest {

    Integer doctorId;
    String time;
    String description;

    public AppointmentRequest(Integer doctorId, String time, String description) {
        this.doctorId = doctorId;
        this.time = time;
        this.description = description;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AppointmentRequest{" +
                "doctorId=" + doctorId +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
