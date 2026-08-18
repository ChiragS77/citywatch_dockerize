import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NagarsevakService {

  private baseUrl = "http://localhost:8080/posts";

  constructor(private http:HttpClient) { }

 // ✅ Stats
// ✅ Get all or filtered complaints
getComplaints() {
  return this.http.get<any[]>(
    `${this.baseUrl}/complaints`,
    {
      withCredentials: true
    }
  );
}

getComplaintCount(status?: string) {
  const params = status 
    ? new HttpParams().set('status', status)
    : new HttpParams();

  return this.http.get<number>(
    `${this.baseUrl}/complaints/count`,
    {
      params,
      withCredentials: true
    }
  );
}

// ✅ Stats
getStats() {
  return this.http.get<any>(
    `${this.baseUrl}/stats`,
    { withCredentials: true }
  );
}

  // ✅ Update status
updateStatus(id: number, status: string) {
  return this.http.put(
    `${this.baseUrl}/${id}/status`,
    {},
    {
      params: { status },
      withCredentials: true
    }
  );
}


getMonthlyData() {
  return this.http.get<any[]>(`${this.baseUrl}/monthly`, {
    withCredentials: true
  });
}

getComplaint(page: number, size: number, status?: string) {
  let params: any = {
    page: page,
    size: size
  };

  if (status) {
    params.status = status;
  }

  return this.http.get<any>(
    `${this.baseUrl}/complaints`,
    {
      params,
      withCredentials: true
    }
  );
}

// 🔥 GET WORKERS (same district/taluka automatically from backend)
getWorkersByLocation() {
  return this.http.get<any[]>(
    `http://localhost:8080/workers/location`,
    { withCredentials: true }
  );
}

getWorkersByType(type: string) {
  return this.http.get<any[]>(
    `http://localhost:8080/worker`,
    {
      params: { type },   // 🔥 IMPORTANT
      withCredentials: true
    }
  );
}

// 🔥 ASSIGN API  
assignWorker(postId: number, workerEmail: string) {
  return this.http.put(
    `http://localhost:8080/posts/${postId}/assign`,
    {},
    {
      params: { workerEmail }, // ✅ FIXED
      withCredentials: true
    }
  );
}

approveWork(postId: number) {
  return this.http.put(
    `${this.baseUrl}/${postId}/approve`,
    {},
    { withCredentials: true }
  );
}

rejectWork(postId: number) {
  return this.http.put(
    `${this.baseUrl}/${postId}/reject`,
    {},
    { withCredentials: true }
  );
}

getPaymentNotifications() {
  return this.http.get<any[]>(
    `http://localhost:8080/notifications/nagarsevak`,
    { withCredentials: true }
  );
}

createOrder(notificationId: number) {
  return this.http.post(
    `http://localhost:8080/notifications/${notificationId}/create-order`,
    {},
    { withCredentials: true }
  );
}

markPaymentSuccess(notificationId: number) {
  return this.http.put(
    `http://localhost:8080/notifications/${notificationId}/payment-success`,
    {},
    { withCredentials: true }
  );
}

}
