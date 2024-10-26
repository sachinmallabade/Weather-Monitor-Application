package com.zeotap.weather.Repository;

import com.zeotap.weather.Model.WeatherData;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    // You can define custom query methods here if needed
	
	 @Query("SELECT w FROM WeatherData w WHERE DATE(w.timestamp) = :date")
	    List<WeatherData> findByDate(@Param("date") LocalDate date);
}
