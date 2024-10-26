package com.zeotap.weather.Service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeotap.weather.Model.WeatherData;
import com.zeotap.weather.Repository.WeatherDataRepository;
import com.zeotap.weather.Service.AlertService;

@Service
public class AlertServiceImpl implements AlertService {

	private final double temperatureThreshold = 35.0;
	private final String alertCondition = "Rain";

	@Autowired
	private WeatherDataRepository weatherDataRepository;

	private final List<String> alerts = new ArrayList<>();

	@Override
	public boolean checkAlertConditions(WeatherData weatherData) {

		return weatherData.getTemperature() > temperatureThreshold || weatherData.getFeelsLike() > temperatureThreshold
				|| weatherData.getMainCondition().equalsIgnoreCase(alertCondition);
	}

	@Override
	public void sendAlert(WeatherData weatherData) {
		if (weatherData.getTemperature() == null) {
			return;
		}

		// String timestamp = weatherData.getFormattedTimestamp(); // Get the formatted
		// timestamp

		if (weatherData.getTemperature() > temperatureThreshold) {
			alerts.add("High temperature alert: " + weatherData.getTemperature());
		}
		if (weatherData.getFeelsLike() > temperatureThreshold) {
			alerts.add("Feels-like temperature alert: " + weatherData.getFeelsLike());
		}
		if (weatherData.getMainCondition().equalsIgnoreCase(alertCondition)) {
			alerts.add("Weather condition alert: " + weatherData.getMainCondition());
		}
	}

	@Override
	public void addWeatherData(WeatherData weatherData) {
		if (checkAlertConditions(weatherData)) {
			sendAlert(weatherData);
		}
	}

	@Override
	public List<String> getAlerts() {
		List<WeatherData> weatherDataList = weatherDataRepository.findAll();
		for (WeatherData weatherData : weatherDataList) {

			sendAlert(weatherData);
		}
		return alerts;
	}
}
