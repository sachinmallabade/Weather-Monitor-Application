package com.zeotap.weather.Controller;

import com.zeotap.weather.Exception.ErrorResponse;

import com.zeotap.weather.Model.WeatherData;
import com.zeotap.weather.Model.WeatherSummary;
import com.zeotap.weather.Service.AlertService;
import com.zeotap.weather.Service.RollupService;
import com.zeotap.weather.Service.WeatherService;
import com.zeotap.weather.dto.DateRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private RollupService rollupService;

    @Autowired
    private AlertService alertService;

    @PostMapping("/data")
    public ResponseEntity<?> addWeatherData(@RequestBody WeatherData weatherData) {
        System.out.println("Received Weather Data: " + weatherData);
        
        // Validate the weather data
        if (weatherData.getFeelsLike() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Feels Like temperature cannot be null", LocalDateTime.now()));
        }

        if (weatherData.getMainCondition() == null || weatherData.getMainCondition().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Main condition cannot be null or empty", LocalDateTime.now()));
        }

        if (weatherData.getTemperature() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Temperature cannot be null", LocalDateTime.now()));
        }

        if (weatherData.getTimestamp() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Timestamp cannot be null", LocalDateTime.now()));
        }

        weatherService.saveWeatherData(weatherData);
        return ResponseEntity.status(HttpStatus.CREATED).body(weatherData);
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<String>> getAlerts() {
        List<String> alerts = alertService.getAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/all-daily-summaries")
    public ResponseEntity<Map<LocalDate, WeatherSummary>> getAllDailySummaries() {
        Map<LocalDate, WeatherSummary> summaries = rollupService.generateAllDailySummaries();
        return ResponseEntity.ok(summaries);
    }

    @GetMapping
    public ResponseEntity<?> getWeather(@RequestParam String city) {
        try {
            WeatherData weatherData = weatherService.fetchWeatherData(city);
            return ResponseEntity.ok(weatherData);
        } catch (RuntimeException e) {
            // Handle runtime exceptions
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
   
    @PostMapping("/summary/daily")
    public ResponseEntity<WeatherSummary> generateDailySummary(@RequestBody DateRequest dateRequest) {
        LocalDate date = dateRequest.getDate();
        WeatherSummary summary = rollupService.generateDailySummary(date);
        return ResponseEntity.ok(summary);
    }

}
