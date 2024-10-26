import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RollupService } from '../../services/rollup.service';
import { WeatherSummary } from '../../models/weather-summary.model';

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.scss']
})
export class SummaryComponent implements OnInit {
  dailySummaries: WeatherSummary[] = [];
  selectedDateSummary: WeatherSummary | null = null;
  errorMessage: string | null = null;
  selectedDate: string = '';
  currentView: 'allSummaries' | 'dailySummary' = 'allSummaries';

  constructor(private rollupService: RollupService, private cdRef: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.fetchAllDailySummaries();
  }

  switchView(view: 'allSummaries' | 'dailySummary') {
    this.currentView = view;
    this.errorMessage = null;
    this.dailySummaries = [];
    this.selectedDateSummary = null;

    if (view === 'allSummaries') {
      this.fetchAllDailySummaries();
    }
  }

  fetchAllDailySummaries() {
    this.rollupService.getAllDailySummaries().subscribe({
      next: (dailySummaries: WeatherSummary[]) => { // Expect an array of WeatherSummary
        console.log('Fetched all summaries:', dailySummaries);
        this.dailySummaries = dailySummaries; // Assign directly to the array
        this.errorMessage = null;
        this.cdRef.detectChanges(); // Trigger change detection if necessary
      },
      error: (error) => {
        console.error('Error fetching all summaries:', error);
        this.errorMessage = 'Unable to fetch all summaries.';
      }
    });
  }

  fetchDailySummaryByDate() {
    if (this.selectedDate) {
      this.rollupService.getDailySummaryByDate(this.selectedDate).subscribe({
        next: (summary: WeatherSummary) => {
          console.log('Fetched summary for date:', this.selectedDate, summary);
          this.selectedDateSummary = summary;
          this.errorMessage = null;
          this.cdRef.detectChanges(); // Trigger change detection
        },
        error: (error) => {
          console.error('Error fetching daily summary:', error);
          this.errorMessage = 'No summary found for this date.';
          this.selectedDateSummary = null;
        }
      });
    }
  }

  getConditionClass(condition: string) {
    switch (condition) {
      case 'Clear': return 'clear';
      case 'Clouds': return 'clouds';
      case 'Rain': return 'rain';
      default: return '';
    }
  }
}
