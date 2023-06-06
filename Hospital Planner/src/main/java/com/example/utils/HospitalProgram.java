package com.example.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HospitalProgram {
        private static final List<String> validAppointmentTimes = Arrays.asList(
                "8:00", "8:20", "8:40", "9:00", "9:20", "9:40", "10:00",
                "10:20", "10:40", "11:00", "11:20", "11:40", "12:00",
                "12:20", "12:40", "13:00", "13:20", "13:40", "14:00",
                "14:20", "14:40", "15:00", "15:20", "15:40"
        );

    /**
     * Retrieves the available appointment times based on the occupied times.
     * @param occupiedTimes The list of occupied appointment times.
     * @return The list of available appointment times.
     */
    public List<String> getAvailableAppointments(List<String> occupiedTimes) {
        List<String> availableTimes = new ArrayList<>(validAppointmentTimes);
        availableTimes.removeAll(occupiedTimes);
        return availableTimes;
    }

    /**
     * Checks if a given time is a valid appointment time.
     * @param time The time to check.
     * @return true if the time is valid, false otherwise.
     */
    public static boolean isValid(String time) {
        return validAppointmentTimes.contains(time);
    }

}