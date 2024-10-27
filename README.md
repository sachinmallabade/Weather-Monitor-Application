# Real-Time Data Processing System for Weather Monitoring

## Introduction
The Real-Time Data Processing System for Weather Monitoring is designed to continuously retrieve weather data from the OpenWeatherMap API and provide summarized insights using rollups and aggregates. The system monitors weather conditions for major metros in India and generates daily summaries, alerts based on configurable thresholds, and visualizations of weather trends.

## Technologies Used
- **Backend**: Spring Boot
- **Database**: MySQL
- **API**: OpenWeatherMap API
- **Programming Language**: Java (JDK 21)

## Features
- Continuous retrieval of real-time weather data from the OpenWeatherMap API.
- Daily weather summary calculations including average, maximum, minimum temperatures, and dominant weather conditions.
- User-configurable alert thresholds for temperature and specific weather conditions.
- Visualization of daily weather summaries and historical trends.
- Support for multiple metro locations in India (Delhi, Mumbai, Chennai, Bangalore, Kolkata, Hyderabad).

## Prerequisites
- **Java Version**: JDK 21
- **MySQL** (for the database)
- **OpenWeatherMap API Key** (sign up for a free API key at [OpenWeatherMap](https://openweathermap.org/))(optional as i have embedded my key in project)
- **Spring Boot** (for running the backend application)

## Setup Instructions
1. Clone the git repository using:
   ```bash
   git clone  https://github.com/sachinmallabade/Weather-Monitor-Application.git
   
2. Open the WeatherMonitorApp folder in STS(spring tool suite)
3. Create a database in MySQL named as weatherdb
4. Run a Project as Spring Boot App
5. Open the command prompt and redirect to weather-monitor-ui folder
6. make sure that you have installed node js
7. install angular -- npm install -g @angular
8. run the command to download all necessary dependencies -- npm install
9. add angular material -- ng add @angular/material
10. run angular app --- ng serve
11. To test the application go to browser and search localhost:4200
