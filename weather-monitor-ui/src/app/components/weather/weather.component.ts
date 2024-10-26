import { Component, OnInit } from '@angular/core';
import { WeatherService } from '../../services/weather.service';
import { WeatherData } from '../../models/weather-data.model';
import { trigger, transition, style, animate, state } from '@angular/animations';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-weather',
  templateUrl: './weather.component.html',
  styleUrls: ['./weather.component.css'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('300ms ease-in', style({ opacity: 1 }))
      ]),
      transition(':leave', [
        animate('300ms ease-out', style({ opacity: 0 }))
      ])
    ]),
    trigger('buttonClick', [
      state('normal', style({ transform: 'scale(1)' })),
      state('clicked', style({ transform: 'scale(0.95)' })),
      transition('normal <=> clicked', animate('100ms ease'))
    ])
  ]
})
export class WeatherComponent implements OnInit {
  city: string = '';
  weatherData: WeatherData | null = null;
  errorMessage: string | null = null;
  buttonState = 'normal';
  alerts: string[] = []; // Initialize alerts array

  constructor(private weatherService: WeatherService, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    // Optionally fetch initial alerts here if needed
  }

  fetchWeather() {
    this.buttonState = 'clicked'; // Change button state to clicked
    setTimeout(() => {
      this.buttonState = 'normal'; // Reset after a short delay
    }, 100);
    
    this.errorMessage = null; // Reset error message
    this.weatherData = null; // Clear previous data
    
    this.weatherService.fetchWeatherData(this.city).subscribe(
      (data) => {
        console.log('Fetched Weather Data:', data); // Log fetched data
        this.weatherData = data; // Assign the received data to weatherData
  
        // Check for alerts after fetching weather data
        this.checkForAlerts(data);
      },
      (error) => {
        this.weatherData = null; // Clear the weather data on error
        this.errorMessage = error; // Set the user-friendly error message
        if (this.errorMessage) { // Check for non-null errorMessage
          this.showAlert(this.errorMessage); // Display the alert with the error message
        }
      }
    );
  }
  

  checkForAlerts(weatherData: WeatherData) {
    if (weatherData) {
      // Define your threshold and alert condition values
      const temperatureThreshold = 35; // example threshold for temperature
      const alertCondition = 'Rain'; // example alert condition for maincondition
      
      // Check against your alert conditions
      if (weatherData.temperature > temperatureThreshold) {
        this.alerts.push(`High temperature alert: ${weatherData.temperature}°C`);
      }
      if (weatherData.feelslike > temperatureThreshold) {
        this.alerts.push(`Feels-like temperature alert: ${weatherData.feelslike}°C`);
      }
      if (weatherData.maincondition === alertCondition) {
        this.alerts.push(`Weather condition alert: ${weatherData.maincondition}`);
      }
    }
  }

  showAlert(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 3000, // Duration in milliseconds
      verticalPosition: 'top',
      horizontalPosition: 'center',
    });
  }
}
