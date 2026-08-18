import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ChartConfiguration } from 'chart.js';
import { AdminService } from 'src/app/services/admin.service';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
 today: Date = new Date();

 alerts: any[] = [];
  stats: any = {};
  systemHealth: any[] = [];
  citizenGrowth: any[] = [];
  wardDistribution: any[] = [];
  recentActivities: any[] = [];

  nagarsevaks: any[] = [];
  nagaradhyaksha: any[] = [];
  workers: any[] = [];

  isLoading = false;

  growthChartData: ChartConfiguration<'line'>['data'] = {
    labels: [],
    datasets: [
      {
        label: 'Citizens',
        data: [],
        tension: 0.4,
        fill: true,
        borderColor: '#2563eb',
        backgroundColor: 'rgba(37, 99, 235, 0.12)',
        pointBackgroundColor: '#2563eb',
        pointBorderColor: '#2563eb',
        pointRadius: 5,
        pointHoverRadius: 6
      }
    ]
  };

  growthChartOptions: ChartConfiguration<'line'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        grid: {
          color: '#e5e7eb'
        }
      },
      x: {
        grid: {
          display: false
        }
      }
    }
  };

  wardChartData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [
      {
        label: 'Citizens',
        data: [],
        backgroundColor: [
          '#2563eb',
          '#059669',
          '#6d28d9',
          '#f97316',
          '#ef4444',
          '#0891b2',
          '#84cc16',
          '#ec4899'
        ],
        borderRadius: 6,
        barThickness: 42
      }
    ]
  };

  wardChartOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        grid: {
          color: '#e5e7eb'
        }
      },
      x: {
        grid: {
          display: false
        }
      }
    }
  };

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadDashboard();
    this.loadAlerts();
  }

  loadDashboard() {
    this.isLoading = true;

    this.loadStats();
    this.loadSystemHealth();
    this.loadCitizenGrowth();
    this.loadWardDistribution();
    this.loadRecentActivities();

    this.isLoading = false;
  }

  loadStats() {
    this.adminService.getStats().subscribe({
      next: (res) => {
        this.stats = res;
      },
      error: (err) => console.error('Stats error:', err)
    });
  }

  loadSystemHealth() {
    this.adminService.getSystemHealth().subscribe({
      next: (res) => {
        this.systemHealth = res;
      },
      error: (err) => console.error('System health error:', err)
    });
  }

  loadCitizenGrowth() {
    this.adminService.getCitizenGrowth().subscribe({
      next: (res) => {
        this.citizenGrowth = res;
        this.prepareGrowthChart();
      },
      error: (err) => console.error('Citizen growth error:', err)
    });
  }

  loadWardDistribution() {
    this.adminService.getWardDistribution().subscribe({
      next: (res) => {
        this.wardDistribution = res;
        this.prepareWardChart();
      },
      error: (err) => console.error('Ward distribution error:', err)
    });
  }

  loadRecentActivities() {
    this.adminService.getRecentActivities().subscribe({
      next: (res) => {
        this.recentActivities = res;
      },
      error: (err) => console.error('Recent activities error:', err)
    });
  }

  prepareGrowthChart() {
    this.growthChartData = {
      labels: this.citizenGrowth.map(g => g.month),
      datasets: [
        {
          label: 'Citizens',
          data: this.citizenGrowth.map(g => g.citizens),
          tension: 0.4,
          fill: true,
          borderColor: '#2563eb',
          backgroundColor: 'rgba(37, 99, 235, 0.12)',
          pointBackgroundColor: '#2563eb',
          pointBorderColor: '#2563eb',
          pointRadius: 5,
          pointHoverRadius: 6
        }
      ]
    };
  }

  prepareWardChart() {
    this.wardChartData = {
      labels: this.wardDistribution.map(w => 'Ward ' + w.wardNo),
      datasets: [
        {
          label: 'Citizens',
          data: this.wardDistribution.map(w => w.citizens),
          backgroundColor: [
            '#2563eb',
            '#059669',
            '#6d28d9',
            '#f97316',
            '#ef4444',
            '#0891b2',
            '#84cc16',
            '#ec4899'
          ],
          borderRadius: 6,
          barThickness: 42
        }
      ]
    };
  }

  loadNagarsevaks() {
    this.adminService.getNagarsevaks().subscribe({
      next: (res) => this.nagarsevaks = res,
      error: (err) => console.error(err)
    });
  }

  loadNagaradhyaksha() {
    this.adminService.getNagaradhyaksha().subscribe({
      next: (res) => this.nagaradhyaksha = res,
      error: (err) => console.error(err)
    });
  }

  loadWorkers() {
    this.adminService.getWorkers().subscribe({
      next: (res) => this.workers = res,
      error: (err) => console.error(err)
    });
  }

  deactivateWorker(id: number) {
    this.adminService.deactivateWorker(id).subscribe({
      next: () => {
        alert('Worker deactivated ✅');
        this.loadWorkers();
      },
      error: (err) => console.error(err)
    });
  }

  deletePoliticalUser(id: number) {
    if (!confirm('Are you sure you want to delete this user?')) return;

    this.adminService.deletePoliticalUser(id).subscribe({
      next: () => {
        alert('User deleted ✅');
        this.loadNagarsevaks();
        this.loadNagaradhyaksha();
        this.loadStats();
      },
      error: (err) => console.error(err)
    });
  }

  loadAlerts() {
  this.adminService.getAlerts().subscribe({
    next: res => this.alerts = res,
    error: err => console.error(err)
  });
}

  
}
