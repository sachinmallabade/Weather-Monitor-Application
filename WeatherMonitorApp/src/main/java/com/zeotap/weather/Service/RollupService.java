package com.zeotap.weather.Service;



import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.zeotap.weather.Model.WeatherData;
import com.zeotap.weather.Model.WeatherSummary;

public interface RollupService {
    /**
     * Calculates the daily summary (e.g., average, max, min temperatures) based on the provided weather data.
     * 
     * @param weatherDataList A list of weather data entries for the day.
     * @return The WeatherSummary object containing the aggregate information.
     */
    WeatherSummary calculateDailySummary(List<WeatherData> weatherDataList);
    public WeatherSummary generateDailySummary(LocalDate date);
    public void addWeatherData(WeatherData weatherData);
    public Map<LocalDate, WeatherSummary> generateAllDailySummaries();
}
