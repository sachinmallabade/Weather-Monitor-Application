package com.zeotap.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeotap.weather.Model.WeatherData;
import com.zeotap.weather.Model.WeatherSummary;
import com.zeotap.weather.Repository.WeatherDataRepository;
import com.zeotap.weather.Repository.WeatherSummaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherMonitorAppIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private WeatherSummaryRepository weatherSummaryRepository;

    private WeatherData sampleWeatherData;

    @BeforeEach
    void setUp() {
        weatherDataRepository.deleteAll();
        weatherSummaryRepository.deleteAll();

        sampleWeatherData = new WeatherData();
        sampleWeatherData.setTemperature(30.0);
        sampleWeatherData.setFeelsLike(29.0);
        sampleWeatherData.setMainCondition("Rain");
        sampleWeatherData.setTimestamp(LocalDateTime.now());

        weatherDataRepository.save(sampleWeatherData);
    }

    @Test
    void testAddWeatherDataAndGetAlerts() throws Exception {
        // Adding new weather data
        mockMvc.perform(post("/weather/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleWeatherData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature", is(30.0)))
                .andExpect(jsonPath("$.mainCondition", is("Rain")));

        // Fetch alerts after adding data
        mockMvc.perform(get("/weather/alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))) // Adjust based on actual alert generation logic
                .andExpect(jsonPath("$[0]", is("Weather condition alert: Rain on " + sampleWeatherData.getTimestamp())));
    }

    @Test
    void testGetWeatherSummaryForSpecificDate() throws Exception {
        LocalDate summaryDate = LocalDate.now();

        // Generate a daily summary
        mockMvc.perform(get("/weather/daily-summary")
                .param("date", summaryDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date", is(summaryDate.toString())))
                .andExpect(jsonPath("$.averageTemperature", is(30.0)))
                .andExpect(jsonPath("$.dominantCondition", is("Rain")));
    }

    @Test
    void testGetAllDailySummaries() throws Exception {
        LocalDate summaryDate1 = LocalDate.now().minusDays(1);
        LocalDate summaryDate2 = LocalDate.now();

        // Generate daily summaries
        weatherSummaryRepository.save(new WeatherSummary(summaryDate1, 25.0, 30.0, 20.0, "Clear"));
        weatherSummaryRepository.save(new WeatherSummary(summaryDate2, 28.0, 32.0, 24.0, "Rain"));

        mockMvc.perform(get("/weather/daily-summaries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].date", is(summaryDate1.toString())))
                .andExpect(jsonPath("$[1].date", is(summaryDate2.toString())));
    }

    @Test
    void testFetchWeatherDataFromAPI() throws Exception {
        String city = "Berlin";
        // Assuming WeatherServiceImpl has logic to handle live data retrieval from OpenWeatherMap

        mockMvc.perform(get("/weather/fetch")
                .param("city", city))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mainCondition").exists())
                .andExpect(jsonPath("$.temperature").exists());
    }
}
