package com.zeotap.weather.Service;

import java.util.List;

import com.zeotap.weather.Model.WeatherData;

public interface AlertService {
    /**
     * Checks if any alert conditions (e.g., temperature thresholds) are met based on the latest weather data.
     * 
     * @param weatherData The latest weather data to evaluate.
     * @return A boolean indicating if an alert should be triggered.
     */
    boolean checkAlertConditions(WeatherData weatherData);

    /**
     * Sends an alert if conditions are met (e.g., via console or email).
     * 
     * @param weatherData The weather data that triggered the alert.
     */
    void sendAlert(WeatherData weatherData);
    List<String> getAlerts();
    public void addWeatherData(WeatherData weatherData);
}

