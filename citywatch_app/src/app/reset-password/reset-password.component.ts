import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { NgClass } from '@angular/common';


@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent {

   resetForm: FormGroup;
  submitted = false;
  loading = false;
  showNewPassword = false;
showConfirmPassword = false;

  token = '';
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {
    this.resetForm = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';

    if (!this.token) {
      this.errorMessage = 'Invalid reset password link.';
    }
  }

  toggleNewPassword() {
  this.showNewPassword = !this.showNewPassword;
}

toggleConfirmPassword() {
  this.showConfirmPassword = !this.showConfirmPassword;
}

  resetPassword() {
    this.submitted = true;
    this.successMessage = '';
    this.errorMessage = '';

    if (this.resetForm.invalid) {
      return;
    }

    const newPassword = this.resetForm.value.newPassword;
    const confirmPassword = this.resetForm.value.confirmPassword;

    if (newPassword !== confirmPassword) {
      this.errorMessage = 'Password and confirm password do not match.';
      return;
    }

    this.loading = true;

    this.authService.resetPassword(this.token, newPassword).subscribe({
      next: (res) => {
        this.loading = false;
        this.successMessage = res.message || 'Password reset successfully.';

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.message || err.error || 'Failed to reset password.';
      }
    });
  }
}
