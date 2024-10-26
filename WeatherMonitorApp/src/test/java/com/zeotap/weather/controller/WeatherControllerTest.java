package com.zeotap.weather.controller;

import com.zeotap.weather.Controller.WeatherController;


import com.zeotap.weather.Model.WeatherData;
import com.zeotap.weather.Model.WeatherSummary;
import com.zeotap.weather.Service.AlertService;
import com.zeotap.weather.Service.RollupService;
import com.zeotap.weather.Service.WeatherService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @MockBean
    private RollupService rollupService;

    @MockBean
    private AlertService alertService;

    @Test
    void testAddWeatherData_ValidData() throws Exception {
        WeatherData weatherData = new WeatherData();
        weatherData.setMainCondition("Clear");
        weatherData.setTemperature(20.5);
        weatherData.setFeelsLike(18.5);
        weatherData.setTimestamp(LocalDateTime.now());

        when(weatherService.saveWeatherData(weatherData)).thenReturn(weatherData);

        mockMvc.perform(post("/api/weather/data")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"maincondition\": \"Clear\", \"temperature\": 20.5, \"feelslike\": 18.5, \"timestamp\": \"2024-10-25T10:15:30\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mainCondition").value("Clear"));
    }

    @Test
    void testAddWeatherData_MissingField() throws Exception {
        mockMvc.perform(post("/api/weather/data")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"maincondition\": \"Clear\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Temperature cannot be null"));
    }

    @Test
    void testGetAlerts() throws Exception {
        when(alertService.getAlerts()).thenReturn(List.of("Heavy rain warning", "High temperature warning"));

        mockMvc.perform(get("/api/weather/alerts")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Heavy rain warning"))
                .andExpect(jsonPath("$[1]").value("High temperature warning"));
    }


    @Test
    void testGetAllDailySummaries() throws Exception {
        Map<LocalDate, WeatherSummary> summaries = Map.of(
                LocalDate.parse("2024-10-24"), new WeatherSummary(LocalDate.parse("2024-10-24"), 18.0, 20.0, 16.0, "Cloudy"),
                LocalDate.parse("2024-10-25"), new WeatherSummary(LocalDate.parse("2024-10-25"), 20.0, 25.0, 15.0, "Clear")
        );
        when(rollupService.generateAllDailySummaries()).thenReturn(summaries);

        mockMvc.perform(get("/api/weather/all-daily-summaries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['2024-10-24'].averageTemperature").value(18.0))
                .andExpect(jsonPath("$.['2024-10-25'].averageTemperature").value(20.0));
    }

    @Test
    void testGetWeather_CityFound() throws Exception {
        WeatherData weatherData = new WeatherData();
        weatherData.setMainCondition("Rain");
        weatherData.setTemperature(21.0);
        weatherData.setFeelsLike(19.0);
        weatherData.setTimestamp(LocalDateTime.now());

        when(weatherService.fetchWeatherData("San Francisco")).thenReturn(weatherData);

        mockMvc.perform(get("/api/weather?city=San Francisco")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mainCondition").value("Rain"))
                .andExpect(jsonPath("$.temperature").value(21.0));
    }

    @Test
    void testGetWeather_CityNotFound() throws Exception {
        when(weatherService.fetchWeatherData("Unknown City")).thenThrow(new RuntimeException("City not found"));

        mockMvc.perform(get("/api/weather?city=Unknown City")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("City not found"));
    }

    @Test
    void testGenerateDailySummary() throws Exception {
        WeatherSummary summary = new WeatherSummary(LocalDate.parse("2024-10-25"), 21.0, 23.0, 18.0, "Clear");
        when(rollupService.generateDailySummary(LocalDate.parse("2024-10-25"))).thenReturn(summary);

        mockMvc.perform(post("/api/weather/summary/daily")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"date\": \"2024-10-25\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageTemperature").value(21.0))
                .andExpect(jsonPath("$.maxTemperature").value(23.0))
                .andExpect(jsonPath("$.minTemperature").value(18.0))
                .andExpect(jsonPath("$.dominantCondition").value("Clear"));
    }

}
