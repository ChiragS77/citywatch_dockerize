import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
selector: 'app-login',
templateUrl: './login.component.html',
styleUrls: ['./login.component.css']
})
export class LoginComponent {

loginForm: FormGroup;

submitted=false;

showPassword=false;

loading=false;

loginError='';

constructor(private fb:FormBuilder,
            private authService:AuthService,
            private router:Router
){

this.loginForm=this.fb.group({

email:['',[Validators.required,Validators.email]],

password:['',Validators.required]

});

}

get f(){
return this.loginForm.controls;
}

togglePassword(){
this.showPassword=!this.showPassword;
}

onSubmit() {

  this.submitted = true;

  if (this.loginForm.invalid) {
    return;
  }

  this.loading = true;

  const data = this.loginForm.value;

  this.authService.login(data).subscribe({

    // ✅ USE DTO (not any)
    next: (res) => {

      this.loading = false;

      console.log(res);

      // ✅ STORE FULL USER (IMPORTANT 🔥)
      localStorage.setItem('user', JSON.stringify(res));
      const uname = localStorage.getItem('user');
      console.log(uname);

      // ✅ STORE ROLE (for guard)
      localStorage.setItem('role', res.role);

       // 🔥 LOAD USER + NAGARSEVAK
  this.authService.loadUserData();

      // ✅ ROUTING
      const routeMap: any = {
        USER: '/',
        NAGARSEVAK: '/nagarsevak',
        NAGARADHYAKSHA: '/nagar-adhyaksha',
        ADMIN: '/admin',
        WORKER:'/worker'
      };

      this.router.navigate([routeMap[res.role]]);
    },

    error: (err) => {

      this.loading = false;

      if (err.error?.field) {

        const field = err.error.field;
        const message = err.error.message;

        this.loginForm.get(field)?.setErrors({ serverError: message });

      } else {
        this.loginError = "Login failed. Please try again.";
      }
    }
  });
}

}