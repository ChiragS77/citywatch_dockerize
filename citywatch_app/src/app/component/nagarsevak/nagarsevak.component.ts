import { Component } from '@angular/core';
import { NagarsevakService } from 'src/app/services/nagarsevak.service';
import { ChartConfiguration, ChartType } from 'chart.js';


@Component({
  selector: 'app-nagarsevak',
  templateUrl: './nagarsevak.component.html',
  styleUrls: ['./nagarsevak.component.css']
})
export class NagarsevakComponent {

   complaints: any[] = [];
  workers: any[] = [];

  selectedComplaint: any;

  showAssignModal = false;
  isLoadingWorkers = false;

  currentPage = 0;
  pageSize = 10;
  totalPages = 0;

  stats: any = {};
  selectedStatus = '';
  

  constructor(private service: NagarsevakService) {}

  ngOnInit() {
    this.refreshDashboard();
  }

  loadStats() {
    this.service.getStats().subscribe(res => this.stats = res);
  }

  loadComplaints() {
    console.log("methods starts...");
    this.service.getComplaint(this.currentPage, this.pageSize, this.selectedStatus)
      .subscribe(res => {
        console.log(res.content);
        this.complaints = res.content;
        this.totalPages = res.totalPages;

      });
  }

  filter(status: string) {
    this.selectedStatus = status;
    this.currentPage = 0;
    this.loadComplaints();
  }

  // ================= ASSIGN =================
 openAssignModal(complaint: any) {
  this.selectedComplaint = complaint;
  this.showAssignModal = true;
  this.isLoadingWorkers = true;


  console.log("Complaint:", complaint);

  // const type = (complaint.workType || this.mapComplaintToWorkType(complaint)).toUpperCase();
  const rawType = complaint.workType || this.mapComplaintToWorkType(complaint);

if (!rawType) {
  alert("Work type not detected ❌");
  return;
}

const type = rawType.toUpperCase();

  this.service.getWorkersByType(type)
    .subscribe({
      next: (res) => {
        this.workers = res;
        this.isLoadingWorkers = false;
      },
      error: (err) => {
        console.error("Failed to load workers", err);
        this.isLoadingWorkers = false;
      }
    });
}


  closeAssignModal() {
  this.showAssignModal = false;
  this.workers = [];
  this.selectedComplaint = null;
}

  assignWorker(worker: any) {

  console.log(this.selectedComplaint.id);
  console.log(worker.email);

  this.service.assignWorker(
    this.selectedComplaint.id,
    worker.email
  ).subscribe({

    next: () => {

      alert("Worker Assigned ✅");

      this.closeAssignModal();

      // 🔥 FULL DASHBOARD REFRESH
      this.refreshDashboard();
    },

    error: () => {
      alert("Assignment failed ❌");
    }

  });
}

  // ================= STATUS =================
  updateStatus(id: number, status: string) {

  this.service.updateStatus(id, status)
    .subscribe(() => {

      this.refreshDashboard();

    });
}

  // ================= PAGINATION =================
  nextPage() {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadComplaints();
    }
  }

  prevPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadComplaints();
    }
  }

  mapComplaintToWorkType(c: any): string {

  const text = (c.caption || '').toLowerCase();

  if (text.includes('light') || text.includes('electric')) {
    return 'ELECTRICIAN';
  }

  if (text.includes('water') || text.includes('pipe')) {
    return 'PLUMBER';
  }

  if (text.includes('wood') || text.includes('door')) {
    return 'CARPENTER';
  }

  return 'CLEANER'; // default
}

refreshDashboard() {
  this.loadStats();
  this.loadComplaints();
}

}
