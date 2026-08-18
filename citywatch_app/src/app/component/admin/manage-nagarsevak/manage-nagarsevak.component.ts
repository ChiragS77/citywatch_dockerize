import { Component } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-manage-nagarsevak',
  templateUrl: './manage-nagarsevak.component.html',
  styleUrls: ['./manage-nagarsevak.component.css']
})
export class ManageNagarsevakComponent {


  nagarsevaks: any[] = [];
  filteredNagarsevaks: any[] = [];

  searchText = '';
  isLoading = false;
  showAddModal = false;

  formData: any = {
    name: '',
    email: '',
    password: '',
    state: '',
    district: '',
    taluka: '',
    wardNo: '',
    partyName: ''
  };

  partyImage: File | null = null;

  stats = {
    total: 0,
    wardsCovered: 0,
    active: 0
  };

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadNagarsevaks();
  }

  loadNagarsevaks() {
    this.isLoading = true;

    this.adminService.getNagarsevaks().subscribe({
      next: (res: any[]) => {
        this.nagarsevaks = res || [];
        this.filteredNagarsevaks = this.nagarsevaks;
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
    this.stats.total = this.nagarsevaks.length;
    this.stats.active = this.nagarsevaks.length;
    this.stats.wardsCovered = new Set(this.nagarsevaks.map(n => n.wardNo)).size;
  }

  openAddModal() {
    this.showAddModal = true;
  }

  closeAddModal() {
    this.showAddModal = false;
    this.resetForm();
  }

  resetForm() {
    this.formData = {
      name: '',
      email: '',
      password: '',
      state: '',
      district: '',
      taluka: '',
      wardNo: '',
      partyName: ''
    };
    this.partyImage = null;
  }

  onImageSelect(event: any) {
    this.partyImage = event.target.files[0];
  }

  createNagarsevak() {
    if (
      !this.formData.name ||
      !this.formData.email ||
      !this.formData.password ||
      !this.formData.state ||
      !this.formData.district ||
      !this.formData.taluka ||
      !this.formData.wardNo
    ) {
      alert('Please fill all required fields');
      return;
    }

    const data = new FormData();

    data.append('name', this.formData.name);
    data.append('email', this.formData.email);
    data.append('password', this.formData.password);
    data.append('state', this.formData.state);
    data.append('district', this.formData.district);
    data.append('taluka', this.formData.taluka);
    data.append('wardNo', this.formData.wardNo);
    data.append('partyName', this.formData.partyName);

    if (this.partyImage) {
      data.append('partyImage', this.partyImage);
    }

    this.adminService.createNagarsevak(data).subscribe({
      next: () => {
        alert('Nagarsevak created successfully ✅');
        this.closeAddModal();
        this.loadNagarsevaks();
      },
      error: (err) => {
        console.error(err);
        alert(err.error?.message || err.error || 'Failed to create Nagarsevak');
      }
    });
  }

  deleteNagarsevak(id: number) {
    if (!confirm('Are you sure you want to delete this Nagarsevak?')) return;

    this.adminService.deleteNagarsevak(id).subscribe({
      next: () => {
        alert('Nagarsevak deleted successfully ✅');
        this.loadNagarsevaks();
      },
      error: (err) => {
        console.error(err);
        alert('Failed to delete Nagarsevak');
      }
    });
  }

  applyFilter() {
    const search = this.searchText.toLowerCase();

    this.filteredNagarsevaks = this.nagarsevaks.filter(n => {
      return (
        n.name?.toLowerCase().includes(search) ||
        n.email?.toLowerCase().includes(search) ||
        n.state?.toLowerCase().includes(search) ||
        n.district?.toLowerCase().includes(search) ||
        n.taluka?.toLowerCase().includes(search) ||
        n.wardNo?.toString().includes(search)
      );
    });
  }

}
