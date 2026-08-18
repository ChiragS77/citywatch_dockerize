import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MessageResponse } from '../dtos/messageresponse';
import { UserProfile } from '../dtos/userprofile';
import { LoginResponse } from '../dtos/loginresponse';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

    nagarsevak: UserProfile | null = null;
currentUser: UserProfile | null = null;


   private baseUrl = "http://localhost:8080/api/user";

   private adminUrl = "http://localhost:8080/admin";

  constructor(private http: HttpClient) {}

  checkEmail(email: string) {
    return this.http.get(`${this.baseUrl}/check-email?email=${email}`);
  }

  register(data: any){
    return this.http.post<MessageResponse>(`${this.baseUrl}/register`, data,{withCredentials:true}

    );
  }
  
    login(data: any) {
  return this.http.post<LoginResponse>(
    `${this.baseUrl}/login`,
    data,
    { withCredentials: true }
  );
}

logout() {
  console.log("Enter into method....");
  return this.http.post(
    `${this.baseUrl}/logout`,
    {},
    { withCredentials: true }
  );
}

 getProfile() {
  return this.http.get<UserProfile>(
    `${this.baseUrl}/profile`,
    { withCredentials: true,
        
     }
  );
}

forgotPassword(email: string) {
  return this.http.post<any>(
    `${this.baseUrl}/forgot-password`,
    { email },
    { withCredentials: true }
  );
}
resetPassword(token: string, newPassword: string) {
  return this.http.post<any>(
    `${this.baseUrl}/reset-password`,
    { token, newPassword },
    { withCredentials: true }
  );
}


 getNagarsevak() {
    return this.http.get<UserProfile>(
      `${this.baseUrl}/nagarsevak`,
      { withCredentials: true }
    );
  }

  loadUserData() {

  this.getProfile().subscribe({
    next: (user) => {

      this.currentUser = user;
      localStorage.setItem('user', JSON.stringify(user));

      // 🔥 LOAD NAGARSEVAK
      this.getNagarsevak().subscribe({
        next: (n) => {
          this.nagarsevak = n;
        },
        error: () => {
          this.nagarsevak = null; // safe fallback
        }
      });

    }
  });
}

  uploadProfileImage(file: File) {
  const formData = new FormData();
  formData.append('image', file);

  return this.http.post(
    `${this.baseUrl}/profile/image`,
    formData,
    {
      withCredentials: true,
      responseType: 'text' 
    }
  );
}

updateProfile(data: any) {
  return this.http.put<UserProfile>(
    `${this.baseUrl}/profile`,
    data,
    { withCredentials: true }
  );
}

getCitizens() {
  return this.http.get<any[]>(
    `${this.baseUrl}/users`,
    { withCredentials: true }
  );
}


}
