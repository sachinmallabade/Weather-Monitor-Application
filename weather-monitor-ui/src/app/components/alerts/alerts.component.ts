import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../services/alert.service'; // Adjust path as necessary

@Component({
  selector: 'app-alerts',
  templateUrl: './alerts.component.html',
  styleUrls: ['./alerts.component.css']
})
export class AlertsComponent implements OnInit {
  alerts: string[] = []; // Assume this is your alerts data
  paginatedAlerts: string[] = [];
  currentPage: number = 1;
  alertsPerPage: number = 10; // Number of alerts to display per page
  totalAlerts: number = 0;

  constructor(private alertService: AlertService) {}

  ngOnInit(): void {
    this.loadAlerts();
  }

  loadAlerts() {
    this.alertService.getAlerts().subscribe((data: string[]) => {
      this.alerts = data;
      this.totalAlerts = this.alerts.length;
      this.paginateAlerts();
    });
  }

  paginateAlerts() {
    const start = (this.currentPage - 1) * this.alertsPerPage;
    const end = start + this.alertsPerPage;
    this.paginatedAlerts = this.alerts.slice(start, end);
  }

  changePage(page: number) {
    this.currentPage = page;
    this.paginateAlerts();
  }

  get totalPages(): number {
    return Math.ceil(this.totalAlerts / this.alertsPerPage);
  }
}
