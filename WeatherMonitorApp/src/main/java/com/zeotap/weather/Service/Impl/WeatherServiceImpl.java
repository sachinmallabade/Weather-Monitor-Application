package com.zeotap.weather.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.zeotap.weather.Config.OpenWeatherMapConfig;
import com.zeotap.weather.Model.WeatherData;
import com.zeotap.weather.Repository.WeatherDataRepository;
import com.zeotap.weather.Service.AlertService;
import com.zeotap.weather.Service.WeatherService;

@Service
public class WeatherServiceImpl implements WeatherService {

	@Value("${weather.api.url}")
	private String apiUrl;

	@Value("${weather.api.key}")
	private String apiKey;

	private String city;

	@Autowired
	@Qualifier("customRestTemplate")
	private RestTemplate restTemplate;

	@Autowired
	private OpenWeatherMapConfig config;

	@Autowired
	private WeatherDataRepository weatherDataRepository;
	
	@Autowired
    private AlertService alertService;

	private WeatherData lastFetchedWeatherData;

	@Override
	public WeatherData getLastFetchedWeatherData() {
		return lastFetchedWeatherData;
	}

	@Override
	public WeatherData saveWeatherData(WeatherData weatherData) {
	    if (weatherData.getFeelsLike() == null) {
	        // Set a default value or handle the error
	        throw new IllegalArgumentException("Feels Like temperature cannot be null");
	    }
	  return weatherDataRepository.save(weatherData);
	}


	 @Override
	    public WeatherData fetchWeatherData(String city) {
	        String url = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, city, apiKey);

	        try {
	            WeatherData weatherData = restTemplate.getForObject(url, WeatherData.class);
	            if (weatherData == null) {
	                throw new RuntimeException("Failed to fetch weather data");
	            }
	            // Save the fetched data
	            saveWeatherData(weatherData);

	            // Check and send alerts based on the fetched data
	            if (alertService.checkAlertConditions(weatherData)) {
	                alertService.sendAlert(weatherData);
	            }
	            return weatherData;

	        } catch (HttpClientErrorException e) {
	            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
	                throw new RuntimeException("City not found: " + city);
	            } else {
	                throw new RuntimeException("Error fetching weather data: " + e.getMessage());
	            }
	        } catch (Exception e) {
	            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
	        }
	    
	}
}
