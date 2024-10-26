package com.zeotap.weather.service;

import com.zeotap.weather.Config.OpenWeatherMapConfig;
import com.zeotap.weather.Model.WeatherData;
import com.zeotap.weather.Repository.WeatherDataRepository;
import com.zeotap.weather.Service.AlertService;
import com.zeotap.weather.Service.Impl.WeatherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherServiceImplTest {

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Mock
    private WeatherDataRepository weatherDataRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OpenWeatherMapConfig config;

    @Mock
    private AlertService alertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveWeatherData_ValidData() {
        WeatherData weatherData = new WeatherData();
        weatherData.setFeelsLike(20.0);
        weatherData.setTemperature(25.0);

        when(weatherDataRepository.save(weatherData)).thenReturn(weatherData);
        WeatherData savedData = weatherService.saveWeatherData(weatherData);

        assertNotNull(savedData);
        assertEquals(25.0, savedData.getTemperature());
    }

    @Test
    void testSaveWeatherData_NullFeelsLike() {
        WeatherData weatherData = new WeatherData();
        weatherData.setTemperature(25.0);

        assertThrows(IllegalArgumentException.class, () -> weatherService.saveWeatherData(weatherData));
    }

    @Test
    void testFetchWeatherData_CityFound() {
        WeatherData weatherData = new WeatherData();
        weatherData.setTemperature(25.0);

        String city = "London";
        when(restTemplate.getForObject(anyString(), eq(WeatherData.class))).thenReturn(weatherData);
        when(weatherDataRepository.save(weatherData)).thenReturn(weatherData);

        WeatherData fetchedData = weatherService.fetchWeatherData(city);
        assertNotNull(fetchedData);
        assertEquals(25.0, fetchedData.getTemperature());
    }

    @Test
    void testFetchWeatherData_CityNotFound() {
        String city = "UnknownCity";
        when(restTemplate.getForObject(anyString(), eq(WeatherData.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        assertThrows(RuntimeException.class, () -> weatherService.fetchWeatherData(city));
    }
}
