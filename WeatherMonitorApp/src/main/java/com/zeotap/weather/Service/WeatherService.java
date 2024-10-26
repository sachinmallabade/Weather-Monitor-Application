package com.zeotap.weather.Service;

import com.zeotap.weather.Model.WeatherData;

public interface WeatherService {

	WeatherData fetchWeatherData(String city);

	WeatherData saveWeatherData(WeatherData weatherData);

	WeatherData getLastFetchedWeatherData();
}
