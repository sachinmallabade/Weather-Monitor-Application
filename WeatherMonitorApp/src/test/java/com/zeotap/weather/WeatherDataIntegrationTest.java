package com.zeotap.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeotap.weather.Model.WeatherData;
import com.zeotap.weather.Repository.WeatherDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherDataIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    private WeatherData sampleWeatherData;

    @BeforeEach
    void setUp() {
        weatherDataRepository.deleteAll();

        sampleWeatherData = new WeatherData();
        sampleWeatherData.setTemperature(30.0);
        sampleWeatherData.setFeelsLike(29.0);
        sampleWeatherData.setMainCondition("Clear");
        sampleWeatherData.setTimestamp(LocalDateTime.now());
        
        weatherDataRepository.save(sampleWeatherData);
    }

    @Test
    void testAddWeatherData() throws Exception {
        WeatherData newWeatherData = new WeatherData();
        newWeatherData.setTemperature(25.0);
        newWeatherData.setFeelsLike(24.0);
        newWeatherData.setMainCondition("Rain");
        newWeatherData.setTimestamp(LocalDateTime.now());

        mockMvc.perform(post("/weather/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newWeatherData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature", is(25.0)))
                .andExpect(jsonPath("$.mainCondition", is("Rain")));
    }

    @Test
    void testGetWeatherDataById() throws Exception {
        Long weatherDataId = sampleWeatherData.getId();

        mockMvc.perform(get("/weather/{id}", weatherDataId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature", is(30.0)))
                .andExpect(jsonPath("$.mainCondition", is("Clear")));
    }

    @Test
    void testGetAllWeatherData() throws Exception {
        mockMvc.perform(get("/weather/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].temperature", is(30.0)))
                .andExpect(jsonPath("$[0].mainCondition", is("Clear")));
    }

    @Test
    void testDeleteWeatherData() throws Exception {
        Long weatherDataId = sampleWeatherData.getId();

        mockMvc.perform(delete("/weather/{id}", weatherDataId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/weather/{id}", weatherDataId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateWeatherData() throws Exception {
        Long weatherDataId = sampleWeatherData.getId();

        WeatherData updatedWeatherData = new WeatherData();
        updatedWeatherData.setTemperature(35.0);
        updatedWeatherData.setFeelsLike(33.0);
        updatedWeatherData.setMainCondition("Cloudy");
        updatedWeatherData.setTimestamp(LocalDateTime.now());

        mockMvc.perform(put("/weather/update/{id}", weatherDataId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedWeatherData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature", is(35.0)))
                .andExpect(jsonPath("$.mainCondition", is("Cloudy")));
    }
}
