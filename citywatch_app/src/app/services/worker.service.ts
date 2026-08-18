import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { WorkerResponse } from '../dtos/workerresponse';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WorkerService {

  currentView: 'dashboard' | 'profile' | 'ratings' = 'dashboard';

setView(view: any) {
  this.currentView = view;
}
  baseUrl = "http://localhost:8080";

  constructor(private http:HttpClient) { }


  getWorkers(type: string) {
  return this.http.get<WorkerResponse[]>(
    `${this.baseUrl}/workers?type=${type}`,
    { withCredentials: true }
  );
}


getMyProfile():Observable<WorkerResponse> {
  return this.http.get<WorkerResponse>(`${this.baseUrl}/worker/me`, {
    withCredentials: true
  });
}

getMyAssignments() {
  return this.http.get<any[]>(`${this.baseUrl}/assignments/me`, {
    withCredentials: true
  });
}

acceptJob(id: number) {
  return this.http.put(`${this.baseUrl}/assignments/${id}/accept`, {}, {
    withCredentials: true
  });
}

rejectJob(id: number) {
  return this.http.put(`${this.baseUrl}/assignments/${id}/reject`, {}, {
    withCredentials: true
  });
}

// ===================== worker.service.ts =====================
updateInfo(data: any) {
  const formData = new FormData();

  formData.append('name', data.name);
  formData.append('district', data.district);
  formData.append('taluka', data.taluka);
  formData.append('experience', data.experience);
  formData.append('available', data.available);
  formData.append('workType', data.workType);
  formData.append('upiId', data.upiId);

  return this.http.put(`${this.baseUrl}/worker/update-info`, formData, {
    withCredentials: true
  });
}

// 🔥 IMAGE UPLOAD
uploadImage(file: File) {
  const formData = new FormData();
  formData.append('image', file);

  return this.http.post(`${this.baseUrl}/worker/upload-image`, formData, {
    withCredentials: true
  });
}

// 🔥 GET NOTIFICATIONS
getNotifications() {
  return this.http.get<any[]>(
    `${this.baseUrl}/notifications`,
    { withCredentials: true }
  );
}

getComplaintById(id: number) {
  return this.http.get<any>(
    `http://localhost:8080/posts/${id}`,
    { withCredentials: true }
  );
}

// 🔥 UPDATE STATUS (ACCEPT / REJECT)
updateNotificationStatus(id: number, status: string) {
  return this.http.put(
    `${this.baseUrl}/notifications/${id}`,
    {},
    {
      params: { status },
      withCredentials: true
    }
  );
}

// 🔥 UPDATE POST STATUS
updatePostStatus(postId: number, status: string) {
  return this.http.put(
    `http://localhost:8080/posts/${postId}/status`,
    {},
    {
      params: { status },
      withCredentials: true
    }
  );
}

// 🔥 START WORK
startWork(id: number) {
  return this.http.put(
    `http://localhost:8080/posts/${id}/start`,
    {},
    { withCredentials: true }
  );
}


// 🔥 REQUEST COMPLETION (VERY IMPORTANT STEP)
requestCompletion(postId: number) {
  return this.http.put(
    `${this.baseUrl}/posts/${postId}/complete`,
    {},
    { withCredentials: true }
  );
}

completeWork(id: number, amount: number, file: File) {

  const formData = new FormData();

  formData.append('amount', amount.toString()); // 🔥 IMPORTANT
  formData.append('image', file);

  return this.http.put(
    `http://localhost:8080/posts/${id}/complete`,
    formData,
    { withCredentials: true }
  );
}

logout() {
  return this.http.post(
    'http://localhost:8080/worker/logout',
    {},
    { withCredentials: true }
  );
}
}
