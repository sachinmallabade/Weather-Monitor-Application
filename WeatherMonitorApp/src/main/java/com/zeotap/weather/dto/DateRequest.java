package com.zeotap.weather.dto;

import java.time.LocalDate;

public class DateRequest {
    private LocalDate date;

    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
