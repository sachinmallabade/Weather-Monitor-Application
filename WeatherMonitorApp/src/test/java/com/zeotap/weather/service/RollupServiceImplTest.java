package com.zeotap.weather.service;

import com.zeotap.weather.Model.WeatherData;
import com.zeotap.weather.Model.WeatherSummary;
import com.zeotap.weather.Repository.WeatherDataRepository;
import com.zeotap.weather.Repository.WeatherSummaryRepository;
import com.zeotap.weather.Service.Impl.RollupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RollupServiceImplTest {

    @InjectMocks
    private RollupServiceImpl rollupService;

    @Mock
    private WeatherDataRepository weatherDataRepository;

    @Mock
    private WeatherSummaryRepository weatherSummaryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateDailySummary_ValidData() {
        LocalDate date = LocalDate.now();
        WeatherData weatherData1 = new WeatherData(25.0, "Clear", LocalDateTime.now());
        WeatherData weatherData2 = new WeatherData(30.0, "Cloudy", LocalDateTime.now());
        when(weatherDataRepository.findByDate(date)).thenReturn(List.of(weatherData1, weatherData2));

        WeatherSummary summary = rollupService.generateDailySummary(date);
        assertNotNull(summary);
        assertEquals(date, summary.getDate());
    }

    @Test
    void testGenerateDailySummary_NoDataForDate() {
        LocalDate date = LocalDate.now();
        when(weatherDataRepository.findByDate(date)).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () -> rollupService.generateDailySummary(date));
    }

    @Test
    void testGenerateAllDailySummaries() {
        WeatherData weatherData1 = new WeatherData(25.0, "Clear", LocalDateTime.now().minusDays(1));
        WeatherData weatherData2 = new WeatherData(30.0, "Cloudy", LocalDateTime.now());
        when(weatherDataRepository.findAll()).thenReturn(List.of(weatherData1, weatherData2));

        var summaries = rollupService.generateAllDailySummaries();
        assertFalse(summaries.isEmpty());
    }
}
