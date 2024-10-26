package com.zeotap.weather.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "weather_summaries")
public class WeatherSummary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate date;
	private double averageTemperature;
	private double maxTemperature;
	private double minTemperature;
	private String dominantCondition;

	public WeatherSummary() {
	}

	public WeatherSummary(LocalDate date, double averageTemperature, double maxTemperature, double minTemperature,
			String dominantCondition) {
		this.date = date;
		this.averageTemperature = averageTemperature;
		this.maxTemperature = maxTemperature;
		this.minTemperature = minTemperature;
		this.dominantCondition = dominantCondition;
	}
	
	

	

	public WeatherSummary(String dominantCondition,double averageTemperature, double maxTemperature, double minTemperature) {
		super();
		this.averageTemperature = averageTemperature;
		this.maxTemperature = maxTemperature;
		this.minTemperature = minTemperature;
		this.dominantCondition = dominantCondition;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getAverageTemperature() {
		return averageTemperature;
	}

	public void setAverageTemperature(double averageTemperature) {
		this.averageTemperature = averageTemperature;
	}

	public double getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(double maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public double getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(double minTemperature) {
		this.minTemperature = minTemperature;
	}

	public String getDominantCondition() {
		return dominantCondition;
	}

	public void setDominantCondition(String dominantCondition) {
		this.dominantCondition = dominantCondition;
	}

	public static WeatherSummary calculateDailySummary(List<WeatherData> weatherDataList) {
		List<WeatherData> validWeatherDataList = weatherDataList.stream()
				.filter(data -> data != null && data.getTemperature() != null && data.getMainCondition() != null)
				.collect(Collectors.toList());

		double avgTemp = validWeatherDataList.stream().mapToDouble(WeatherData::getTemperature).average()
				.orElse(Double.NaN);

		double maxTemp = validWeatherDataList.stream().mapToDouble(WeatherData::getTemperature).max()
				.orElse(Double.NaN);

		double minTemp = validWeatherDataList.stream().mapToDouble(WeatherData::getTemperature).min()
				.orElse(Double.NaN);

		Map<String, Long> conditionFrequency = validWeatherDataList.stream()
				.collect(Collectors.groupingBy(WeatherData::getMainCondition, Collectors.counting()));

		String dominantCondition = conditionFrequency.entrySet().stream().max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey).orElse("Unknown");

		return new WeatherSummary(LocalDate.now(), avgTemp, maxTemp, minTemp, dominantCondition);
	}

	@Override
	public String toString() {
		return "WeatherSummary{" + "date=" + date + ", averageTemperature=" + averageTemperature + ", maxTemperature="
				+ maxTemperature + ", minTemperature=" + minTemperature + ", dominantCondition='" + dominantCondition
				+ '\'' + '}';
	}
}
