import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { WeatherComponent } from './components/weather/weather.component';
import { AlertsComponent } from './components/alerts/alerts.component';
import { SummaryComponent } from './components/summary/summary.component';
import { MatCardModule } from '@angular/material/card';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule } from '@angular/material/toolbar'; 
import { MatButtonModule } from '@angular/material/button';
import { HttpClientModule } from '@angular/common/http';
import { WeatherService } from './services/weather.service';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    AppComponent,
    WeatherComponent,
    AlertsComponent,
    SummaryComponent
  ],
  imports: [
    BrowserModule,
    MatSnackBarModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatToolbarModule,
    MatButtonModule ,
    HttpClientModule,
    FormsModule 
  ],
  providers: [
    provideAnimationsAsync(),
    WeatherService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
