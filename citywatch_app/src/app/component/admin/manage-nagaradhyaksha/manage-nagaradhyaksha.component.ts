import { Component } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-manage-nagaradhyaksha',
  templateUrl: './manage-nagaradhyaksha.component.html',
  styleUrls: ['./manage-nagaradhyaksha.component.css']
})
export class ManageNagaradhyakshaComponent {


  nagaradhyakshas: any[] = [];
  filteredNagaradhyakshas: any[] = [];

  searchText = '';
  selectedState = '';
  selectedDistrict = '';
  selectedStatus = '';

  showAddModal = false;
  isLoading = false;

  formData: any = {
    name: '',
    email: '',
    password: '',
    state: '',
    district: '',
    taluka: '',
    partyName: ''
  };

  partyImage: File | null = null;

  stats = {
    total: 0,
    active: 0,
    inactive: 0
  };

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadNagaradhyaksha();
  }

  loadNagaradhyaksha() {
    this.isLoading = true;

    this.adminService.getNagaradhyaksha().subscribe({
      next: (res: any[]) => {
        this.nagaradhyakshas = res || [];
        this.filteredNagaradhyakshas = this.nagaradhyakshas;
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
    this.stats.total = this.nagaradhyakshas.length;
    this.stats.active = this.nagaradhyakshas.length;
    this.stats.inactive = 0;
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
      partyName: ''
    };

    this.partyImage = null;
  }

  onImageSelect(event: any) {
    this.partyImage = event.target.files[0];
  }

  createNagaradhyaksha() {
    if (!this.formData.name || !this.formData.email || !this.formData.password) {
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
    data.append('partyName', this.formData.partyName);

    if (this.partyImage) {
      data.append('partyImage', this.partyImage);
    }

    this.adminService.createNagaradhyaksha(data).subscribe({
      next: () => {
        alert('Nagaradhyaksha created successfully ✅');
        this.closeAddModal();
        this.loadNagaradhyaksha();
      },
      error: (err) => {
        console.error(err);
        alert(err.error?.message || err.error || 'Failed to create Nagaradhyaksha');
      }
    });
  }

  deleteNagaradhyaksha(id: number) {
    if (!confirm('Are you sure you want to delete this Nagaradhyaksha?')) return;

    this.adminService.deleteNagaradhyaksha(id).subscribe({
      next: () => {
        alert('Nagaradhyaksha deleted successfully ✅');
        this.loadNagaradhyaksha();
      },
      error: (err) => {
        console.error(err);
        alert('Failed to delete Nagaradhyaksha');
      }
    });
  }

  applyFilter() {
    const search = this.searchText.toLowerCase();

    this.filteredNagaradhyakshas = this.nagaradhyakshas.filter(n => {
      return (
        n.name?.toLowerCase().includes(search) ||
        n.email?.toLowerCase().includes(search) ||
        n.state?.toLowerCase().includes(search) ||
        n.district?.toLowerCase().includes(search) ||
        n.taluka?.toLowerCase().includes(search)
      );
    });
  }

}
