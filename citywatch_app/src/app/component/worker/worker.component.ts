// ===================== worker.component.ts =====================
import { Component, OnInit } from '@angular/core';
import { WorkerService } from 'src/app/services/worker.service';

@Component({
  selector: 'app-worker',
  templateUrl: './worker.component.html'
})
export class WorkerComponent implements OnInit {
   worker: any;
  notifications: any[] = [];

  sidebarOpen = true;
  amount!: number;
  isLoggingOut:boolean = false;

  // 🔥 EDIT PROFILE
  showEditModal = false;
  editData: any = {};
  selectedFile?: File;
  previewUrl: string | null = null;
  isSaving = false;
  showCompleteModal = false;
completionData: any = {};
selectedWorkImage?: File;
currentPostId!: number;
profileMenuOpen = false;

toggleProfileMenu() {
  this.profileMenuOpen = !this.profileMenuOpen;
}

workTypes = [
  'ELECTRICIAN',
  'PLUMBER',
  'CARPENTER',
  'CLEANER',
  'ROAD_REPAIR',
  'GARBAGE_COLLECTION',
  'STREET_LIGHT_MAINTENANCE',
  'WATER_SUPPLY',
  'DRAINAGE',
  'SEWERAGE',
  'TREE_MAINTENANCE',
  'CIVIL_WORK',
  'PEST_CONTROL',
  'PARK_MAINTENANCE'
];
  constructor(private workerService: WorkerService) {}

  ngOnInit(): void {
    this.loadWorker();
    this.loadNotifications();
  }

  // ================= PROFILE =================
  loadWorker() {
    this.workerService.getMyProfile()
      .subscribe(res => this.worker = res);
  }

  openEdit() {
    if (!this.worker) return;

    this.editData = {
      name: this.worker.name || '',
      district: this.worker.district || '',
      taluka: this.worker.taluka || '',
      experience: this.worker.experience || '',
      available: this.worker.available ?? true,
      workType: (this.worker.workType || 'CLEANER').toUpperCase(),
        upiId: this.worker.upiId || ''

    };

    this.showEditModal = true;
  }

  closeEdit() {
    if (!this.isSaving) {
      this.showEditModal = false;
    }
  }

  onImageChange(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    this.selectedFile = file;

    const reader = new FileReader();
    reader.onload = () => this.previewUrl = reader.result as string;
    reader.readAsDataURL(file);
  }

  updateProfile() {
    this.isSaving = true;

    const payload = {
      ...this.editData,
      workType: this.editData.workType.toUpperCase()
    };

    this.workerService.updateInfo(payload).subscribe({
      next: () => {
        if (this.selectedFile) {
          this.workerService.uploadImage(this.selectedFile).subscribe({
            next: () => this.afterSave(),
            error: () => this.handleError()
          });
        } else {
          this.afterSave();
        }
      },
      error: () => this.handleError()
    });
  }

  afterSave() {
    this.isSaving = false;
    this.showEditModal = false;
    this.selectedFile = undefined;
    this.previewUrl = null;
    this.loadWorker();
  }

  handleError() {
    this.isSaving = false;
    alert("Update failed ❌");
  }

  // ================= NOTIFICATIONS =================
  loadNotifications() {
    this.workerService.getNotifications()
      .subscribe(res => {

        this.notifications = res;

        // 🔥 attach complaint details
        this.notifications.forEach(n => {
          this.workerService.getComplaintById(n.complaintId)
            .subscribe(data => {
              n.details = data;
            });
        });

      });
  }

  // ================= ACTIONS =================
  accept(id: number) {
    this.workerService.updateNotificationStatus(id, 'ACCEPTED')
      .subscribe(() => {
        alert("Accepted ✅");
        this.loadNotifications();
      });
  }

  reject(id: number) {
    this.workerService.updateNotificationStatus(id, 'REJECTED')
      .subscribe(() => {
        alert("Rejected ❌");
        this.loadNotifications();
      });
  }



  toggleSidebar() {
    this.sidebarOpen = !this.sidebarOpen;
  }

 

  // 🔥 START WORK
startWork(id: number) {
  this.workerService.startWork(id).subscribe(() => {
    alert("Work Started 🚧");
    this.loadNotifications(); // refresh UI
  });
}


// 🔥 COMPLETE WORK (REQUEST)
completeWork(id: number) {

  if (!this.selectedFile) {
    alert("Upload work image");
    return;
  }

  if (!this.amount) {
    alert("Enter amount");
    return;
  }

  this.workerService
    .completeWork(id, this.amount, this.selectedFile)
    .subscribe(() => {
      alert("Completion requested ⏳");
      this.loadNotifications();
    });
}

// 🔥 OPEN MODAL
openCompleteModal(postId: number) {
  this.currentPostId = postId;
  this.showCompleteModal = true;
}

// 🔥 CLOSE
closeCompleteModal() {
  this.showCompleteModal = false;
  this.completionData = {};
  this.selectedWorkImage = undefined;
}

// 🔥 IMAGE
onCompleteImage(event: any) {
  this.selectedWorkImage = event.target.files[0];
}

// 🔥 SUBMIT
submitCompletion() {

  if (!this.selectedWorkImage) {
    alert("Upload image");
    return;
  }

  if (!this.completionData.amount) {
    alert("Enter amount");
    return;
  }

  this.workerService
    .completeWork(
      this.currentPostId,
      this.completionData.amount,
      this.selectedWorkImage
    )
    .subscribe(() => {
      alert("Completion requested ⏳");
      this.closeCompleteModal();
      this.loadNotifications();
    });
}
logout() {

  this.isLoggingOut = true;

  this.workerService.logout().subscribe({

    next: () => {

      localStorage.clear();
      sessionStorage.clear();

      // 🔥 WAIT 1.5 SECOND
      setTimeout(() => {

        window.location.href = '/login';

      }, 1500);

    },

    error: () => {

      this.isLoggingOut = false;
      alert("Logout failed ❌");

    }

  });

}



}