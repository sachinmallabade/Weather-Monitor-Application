package com.zeotap.weather.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeotap.weather.Model.WeatherData;
import com.zeotap.weather.Model.WeatherSummary;
import com.zeotap.weather.Repository.WeatherDataRepository;
import com.zeotap.weather.Repository.WeatherSummaryRepository;
import com.zeotap.weather.Service.RollupService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RollupServiceImpl implements RollupService {

	@Autowired
	private WeatherDataRepository weatherDataRepository;

	@Autowired
	private WeatherSummaryRepository weatherSummaryRepository;

	@Override
	public WeatherSummary generateDailySummary(LocalDate date) {
		List<WeatherData> weatherDataList = weatherDataRepository.findByDate(date);

		if (weatherDataList.isEmpty()) {
			throw new IllegalArgumentException("No weather data available for the specified date: " + date);
		}

		WeatherSummary summary = WeatherSummary.calculateDailySummary(weatherDataList);

		summary.setDate(date);

		weatherSummaryRepository.save(summary);

		return summary;
	}

	@Override
	public void addWeatherData(WeatherData weatherData) {
		weatherDataRepository.save(weatherData);
	}

	@Override
	public WeatherSummary calculateDailySummary(List<WeatherData> weatherDataList) {
		return WeatherSummary.calculateDailySummary(weatherDataList);
	}

	@Override
	public Map<LocalDate, WeatherSummary> generateAllDailySummaries() {
		List<WeatherData> allWeatherData = weatherDataRepository.findAll();

		Map<LocalDate, List<WeatherData>> dataByDate = allWeatherData.stream()
				.filter(data -> data.getTimestamp() != null) 
				.collect(Collectors.groupingBy(data -> data.getTimestamp().toLocalDate()));

		Map<LocalDate, WeatherSummary> summaries = new HashMap<>();

		for (Map.Entry<LocalDate, List<WeatherData>> entry : dataByDate.entrySet()) {
			LocalDate date = entry.getKey();
			List<WeatherData> weatherDataList = entry.getValue();

			WeatherSummary summary = WeatherSummary.calculateDailySummary(weatherDataList);

			summary.setDate(date);

			weatherSummaryRepository.save(summary);

			summaries.put(date, summary);
		}

		return summaries;
	}

}
