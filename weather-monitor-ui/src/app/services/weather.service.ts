import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { WeatherData } from '../models/weather-data.model';

@Injectable({
  providedIn: 'root',
})
export class WeatherService {
  private baseUrl = 'http://localhost:8090/api/weather'; // Update to your actual API base URL

  constructor(private http: HttpClient) {}

  fetchWeatherData(city: string): Observable<WeatherData> {
    return this.http.get<WeatherData>(`${this.baseUrl}?city=${city}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    // Customize the error message based on error status or other criteria
    let errorMessage = 'Error fetching weather data. Please try again later.';
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred
      errorMessage = `Client-side error: ${error.error.message}`;
    } else {
      // The backend returned an unsuccessful response code
      if (error.status === 404) {
        errorMessage = 'City not found. Please enter a valid city name.';
      } else {
        errorMessage = `Server error: ${error.status}`;
      }
    }
    return throwError(errorMessage);
  }
}
