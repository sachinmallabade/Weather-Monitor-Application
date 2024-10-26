package com.zeotap.weather.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "weather_data")
public class WeatherData {

	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	  @JsonProperty("maincondition")
	    private String mainCondition;

	    @JsonProperty("temperature")
	    private Double temperature;

	    @JsonProperty("feelslike")
	    private Double feelsLike;

	    @JsonProperty("timestamp")
	    private LocalDateTime timestamp;

    public WeatherData() {
    }

    public WeatherData(String mainCondition, double temperature, double feelsLike, LocalDateTime timestamp) {
        this.mainCondition = mainCondition;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.timestamp = timestamp;
    }
    
    

    public WeatherData( Double temperature,String mainCondition, LocalDateTime timestamp) {
		super();
		this.mainCondition = mainCondition;
		this.temperature = temperature;
		this.timestamp = timestamp;
	}

	public Long getId() {
        return id;  
    }

    public void setId(Long id) {
        this.id = id;  
    }

    public String getMainCondition() {
        return mainCondition;
    }

    public void setMainCondition(String mainCondition) {
        this.mainCondition = mainCondition;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getFormattedTimestamp() {
        if (timestamp != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return timestamp.format(formatter);
        }
        return "Unknown time";
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static double convertToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    public static double convertToFahrenheit(double kelvin) {
        return (kelvin - 273.15) * 9 / 5 + 32;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "mainCondition='" + mainCondition + '\'' +
                ", temperature=" + temperature +
                ", feelsLike=" + feelsLike +
                ", timestamp=" + timestamp +
                '}';
    }

    public void setWeather(List<Weather> weatherList) {
        if (weatherList != null && !weatherList.isEmpty()) {
            this.mainCondition = weatherList.get(0).getMain();
        }
    }

    public void setMain(Map<String, Object> main) {
        if (main != null) {
            this.temperature = (Double) main.get("temp");
            this.feelsLike = (Double) main.get("feels_like");
        }
    }
}
