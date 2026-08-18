import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { debounceTime, distinctUntilChanged, of, switchMap } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { LOCATION_DATA } from 'src/app/data/location-data';



@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {


states = Object.keys(LOCATION_DATA);

districts: string[] = [];

talukas: string[] = [];

wards: string[] = [];


   registerForm!: FormGroup;

  emailExists = false;
  emailChecking = false;
  emailAvailable = false;

  constructor(private fb: FormBuilder, private authService: AuthService) {}

  ngOnInit(): void {

   this.registerForm = this.fb.group({
  name: ['', [Validators.required, Validators.minLength(3)]],

  state: ['', Validators.required],
  district: ['', Validators.required],
  taluka: ['', Validators.required],
  wardNo: ['', Validators.required],

  email: ['', [Validators.required, Validators.email]],
  password: ['', [Validators.required, Validators.minLength(8)]],

  age: ['', [Validators.required, Validators.min(18)]],
  dob: ['', Validators.required],

  // 🔥 NEW
  role: ['USER', Validators.required],

  // 🔥 OPTIONAL (for worker only)
  workType: [''],

  terms: [false, Validators.requiredTrue]
});
    this.setupEmailValidation();
  }

  setupEmailValidation() {

  const emailControl = this.registerForm.get('email');

  emailControl?.valueChanges.pipe(

    debounceTime(500),

    distinctUntilChanged(),

    switchMap(email => {

      if (!email || emailControl.invalid) {
        this.emailChecking = false;
        this.emailExists = false;
        this.emailAvailable = false;
        return of(null);
      }

      this.emailChecking = true;

      return this.authService.checkEmail(email);

    })

  ).subscribe((res:any)=>{

    this.emailChecking = false;

    if(!res){
      return;
    }

    if(res.exists){
      this.emailExists = true;
      this.emailAvailable = false;
    }else{
      this.emailExists = false;
      this.emailAvailable = true;
    }

  });

}

 onSubmit() {

  console.log("Click successfully....");
  // if (this.registerForm.invalid) {
  //   this.registerForm.markAllAsTouched();
  //   return;
  // }

  if (this.emailExists) {
    return;
  }

  const formData = this.registerForm.value;

  const registerReq = {
  name: formData.name,
  email: formData.email,
  password: formData.password,

  state: formData.state,
  district: formData.district,
  taluka: formData.taluka,
  wardNo: Number(formData.wardNo),

  dob: formData.dob,
  age: Number(formData.age),

  // 🔥 IMPORTANT
  role: formData.role,

  // optional
  workType: formData.workType
};

  console.log(registerReq);

  this.authService.register(registerReq).subscribe({

    next: (res) => {
      console.log("User Registered", res);
    },

    error: (err) => {
      console.error("Registration failed", err);
    }

  });

}



  get f() {
    return this.registerForm.controls;
  }

 

onStateChange(){

const state = this.registerForm.get('state')?.value;

this.districts = Object.keys(LOCATION_DATA[state] || {});

this.talukas = [];

this.wards = [];

}


onDistrictChange(){

const state = this.registerForm.get('state')?.value;

const district = this.registerForm.get('district')?.value;

this.talukas = Object.keys(LOCATION_DATA[state]?.[district] || {});

this.wards = [];

}


onTalukaChange(){

const state = this.registerForm.get('state')?.value;

const district = this.registerForm.get('district')?.value;

const taluka = this.registerForm.get('taluka')?.value;

this.wards = LOCATION_DATA[state]?.[district]?.[taluka] || [];

}

}
