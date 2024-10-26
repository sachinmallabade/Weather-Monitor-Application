package com.zeotap.weather.service;

import com.zeotap.weather.Model.WeatherData;
import com.zeotap.weather.Repository.WeatherDataRepository;
import com.zeotap.weather.Service.Impl.AlertServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AlertServiceImplTest {

    @InjectMocks
    private AlertServiceImpl alertService;

    @Mock
    private WeatherDataRepository weatherDataRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckAlertConditions_HighTemperature() {
        WeatherData weatherData = new WeatherData();
        weatherData.setTemperature(36.0);
        weatherData.setMainCondition("Clear");

        assertTrue(alertService.checkAlertConditions(weatherData));
    }

    @Test
    void testCheckAlertConditions_RainCondition() {
        WeatherData weatherData = new WeatherData();
        weatherData.setTemperature(25.0);
        weatherData.setMainCondition("Rain");

        assertTrue(alertService.checkAlertConditions(weatherData));
    }

    @Test
    void testSendAlert_AddsAlertForHighTemperature() {
        WeatherData weatherData = new WeatherData();
        weatherData.setTemperature(37.0);
        weatherData.setMainCondition("Clear");
        weatherData.setTimestamp(LocalDateTime.now());

        alertService.sendAlert(weatherData);

        assertFalse(alertService.getAlerts().isEmpty());
    }

    @Test
    void testGetAlerts() {
        when(weatherDataRepository.findAll()).thenReturn(List.of(
                new WeatherData(36.0, "Clear", LocalDateTime.now()),
                new WeatherData(25.0, "Rain", LocalDateTime.now())
        ));

        List<String> alerts = alertService.getAlerts();
        assertEquals(2, alerts.size());
    }
}
