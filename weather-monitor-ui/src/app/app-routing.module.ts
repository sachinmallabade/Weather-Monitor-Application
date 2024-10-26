import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WeatherComponent } from './components/weather/weather.component';
import { AlertsComponent } from './components/alerts/alerts.component';
import { SummaryComponent } from './components/summary/summary.component';

const routes: Routes = [
  { path: '', redirectTo: '/weather', pathMatch: 'full' },
  { path: 'weather', component: WeatherComponent },
  { path: 'alerts', component: AlertsComponent },
  { path: 'summary', component: SummaryComponent },
  { path: '**', redirectTo: '/weather' } // Redirects any unknown routes back to 'weather'
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
