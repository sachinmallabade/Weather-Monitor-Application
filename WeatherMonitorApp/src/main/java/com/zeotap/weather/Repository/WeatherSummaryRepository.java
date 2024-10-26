package com.zeotap.weather.Repository;

import com.zeotap.weather.Model.WeatherSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WeatherSummaryRepository extends JpaRepository<WeatherSummary, Long> {
	List<WeatherSummary> findByDate(LocalDate date);
}
