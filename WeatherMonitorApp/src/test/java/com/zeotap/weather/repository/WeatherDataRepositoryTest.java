package com.zeotap.weather.repository;

import com.zeotap.weather.Model.WeatherData;
import com.zeotap.weather.Repository.WeatherDataRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test") // Uses application-test.properties for an in-memory database like H2
class WeatherDataRepositoryTest {

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    private WeatherData weatherData1;
    private WeatherData weatherData2;

    @BeforeEach
    void setUp() {
        weatherData1 = new WeatherData();
        weatherData1.setTemperature(25.0);
        weatherData1.setFeelsLike(24.0);
        weatherData1.setMainCondition("Clear");
        weatherData1.setTimestamp(LocalDateTime.now().minusDays(1));
        
        weatherData2 = new WeatherData();
        weatherData2.setTemperature(30.0);
        weatherData2.setFeelsLike(29.0);
        weatherData2.setMainCondition("Rain");
        weatherData2.setTimestamp(LocalDateTime.now());

        weatherDataRepository.save(weatherData1);
        weatherDataRepository.save(weatherData2);
    }

    @Test
    void testFindByDate_ReturnsWeatherDataForGivenDate() {
        // Setup: Query for data by specific date
        LocalDateTime targetDate = weatherData1.getTimestamp().toLocalDate().atStartOfDay();
        
        // Exercise: Find data for the given date
        List<WeatherData> result = weatherDataRepository.findByDate(targetDate.toLocalDate());

        // Verify
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(25.0, result.get(0).getTemperature());
    }

    @Test
    void testFindByDate_NoDataForGivenDate() {
        LocalDateTime targetDate = LocalDateTime.now().minusDays(5).toLocalDate().atStartOfDay();
        
        List<WeatherData> result = weatherDataRepository.findByDate(targetDate.toLocalDate());
        
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAll_ReturnsAllWeatherData() {
        List<WeatherData> result = weatherDataRepository.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(weatherData1));
        assertTrue(result.contains(weatherData2));
    }
}
