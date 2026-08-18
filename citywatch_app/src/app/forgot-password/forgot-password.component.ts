import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {


  forgotForm: FormGroup;
  submitted = false;
  loading = false;

  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService
  ) {
    this.forgotForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  sendResetLink() {
    this.submitted = true;
    this.successMessage = '';
    this.errorMessage = '';

    if (this.forgotForm.invalid) {
      return;
    }

    this.loading = true;

    const email = this.forgotForm.value.email;

    this.authService.forgotPassword(email).subscribe({
      next: (res) => {
        this.loading = false;
        this.successMessage = res.message || 'Reset password link sent successfully. Please check your email.';
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.message || 'Failed to send reset link.';
      }
    });
  }
}
