import { Component } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-manage-worker',
  templateUrl: './manage-worker.component.html',
  styleUrls: ['./manage-worker.component.css']
})
export class ManageWorkerComponent {


  workers: any[] = [];
  filteredWorkers: any[] = [];

  searchText = '';
  selectedWorkType = '';
  selectedAvailability = '';

  isLoading = false;

  stats = {
    total: 0,
    active: 0,
    inactive: 0,
    averageRating: 0
  };

  workTypes = [
    'ELECTRICIAN',
    'PLUMBER',
    'CARPENTER',
    'CLEANER',
    'PLUMBER'
  ];

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadWorkers();
  }

  loadWorkers() {
    this.isLoading = true;

    this.adminService.getWorkers().subscribe({
      next: (res: any[]) => {
        this.workers = res || [];
        this.filteredWorkers = this.workers;
        this.calculateStats();
        this.isLoading = false;
      },
      error: (err) => {
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  calculateStats() {
    this.stats.total = this.workers.length;
    this.stats.active = this.workers.filter(w => w.available === true).length;
    this.stats.inactive = this.workers.filter(w => w.available === false).length;

    const totalRating = this.workers.reduce((sum, w) => sum + (w.rating || 0), 0);
    this.stats.averageRating = this.workers.length
      ? Number((totalRating / this.workers.length).toFixed(1))
      : 0;
  }

  applyFilter() {
    const search = this.searchText.toLowerCase();

    this.filteredWorkers = this.workers.filter(w => {
      const user = w.user || {};

      const matchesSearch =
        user.name?.toLowerCase().includes(search) ||
        user.email?.toLowerCase().includes(search) ||
        user.state?.toLowerCase().includes(search) ||
        user.district?.toLowerCase().includes(search) ||
        user.taluka?.toLowerCase().includes(search) ||
        w.workType?.toLowerCase().includes(search);

      const matchesWorkType =
        !this.selectedWorkType || w.workType === this.selectedWorkType;

      const matchesAvailability =
        !this.selectedAvailability ||
        (this.selectedAvailability === 'ACTIVE' && w.available === true) ||
        (this.selectedAvailability === 'INACTIVE' && w.available === false);

      return matchesSearch && matchesWorkType && matchesAvailability;
    });
  }

  resetFilter() {
    this.searchText = '';
    this.selectedWorkType = '';
    this.selectedAvailability = '';
    this.filteredWorkers = this.workers;
  }

  activateWorker(id: number) {
    this.adminService.activateWorker(id).subscribe({
      next: () => {
        alert('Worker activated successfully ✅');
        this.loadWorkers();
      },
      error: (err) => {
        console.error(err);
        alert('Failed to activate worker');
      }
    });
  }

  deactivateWorker(id: number) {
    this.adminService.deactivateWorker(id).subscribe({
      next: () => {
        alert('Worker deactivated successfully ✅');
        this.loadWorkers();
      },
      error: (err) => {
        console.error(err);
        alert('Failed to deactivate worker');
      }
    });
  }

  deleteWorker(id: number) {
    if (!confirm('Are you sure you want to delete this worker account?')) return;

    this.adminService.deleteWorker(id).subscribe({
      next: () => {
        alert('Worker deleted successfully ✅');
        this.loadWorkers();
      },
      error: (err) => {
        console.error(err);
        alert('Failed to delete worker');
      }
    });
  }

}
