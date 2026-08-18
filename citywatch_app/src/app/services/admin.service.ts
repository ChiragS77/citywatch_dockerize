import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private baseUrl = "http://localhost:8080/admin";
    private apiUrl = 'http://localhost:8081/admin';


  constructor(private http:HttpClient) { }

//    createNagarsevak(formData: FormData) {
//   return this.http.post(`${this.baseUrl}/create-nagarsevak`, formData, {
//     withCredentials: true
//   });
// }

// createNagaradhyaksha(formData: FormData) {
//   return this.http.post(`${this.baseUrl}/create-nagaradhyaksha`, formData, {
//     withCredentials: true
//   });
// }


  // ================= DASHBOARD STATS =================
  getStats(): Observable<any> {
    return this.http.get<any>(
      `${this.baseUrl}/stats`,
      { withCredentials: true }
    );
  }

  // ================= SYSTEM HEALTH =================
  getSystemHealth(): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/system-health`,
      { withCredentials: true }
    );
  }

  // ================= CITIZEN GROWTH =================
  getCitizenGrowth(): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/citizen-growth`,
      { withCredentials: true }
    );
  }

  // ================= WARD DISTRIBUTION =================
  getWardDistribution(): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/ward-distribution`,
      { withCredentials: true }
    );
  }

  // ================= RECENT ACTIVITIES =================
  getRecentActivities(): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/recent-activities`,
      { withCredentials: true }
    );
  }

  // ================= POLITICAL USER MANAGEMENT =================
  // createNagarsevak(formData: FormData): Observable<any> {
  //   return this.http.post<any>(
  //     `${this.baseUrl}/create-nagarsevak`,
  //     formData,
  //     { withCredentials: true }
  //   );
  // }

  // createNagaradhyaksha(formData: FormData): Observable<any> {
  //   return this.http.post<any>(
  //     `${this.baseUrl}/create-nagaradhyaksha`,
  //     formData,
  //     { withCredentials: true }
  //   );
  // }

  // getNagarsevaks(): Observable<any[]> {
  //   return this.http.get<any[]>(
  //     `${this.baseUrl}/nagarsevaks`,
  //     { withCredentials: true }
  //   );
  // }

  // getNagaradhyaksha(): Observable<any[]> {
  //   return this.http.get<any[]>(
  //     `${this.baseUrl}/nagaradhyaksha`,
  //     { withCredentials: true }
  //   );
  // }

  updatePoliticalUser(id: number, formData: FormData): Observable<any> {
    return this.http.put<any>(
      `${this.baseUrl}/political-user/${id}`,
      formData,
      { withCredentials: true }
    );
  }

  deletePoliticalUser(id: number): Observable<any> {
    return this.http.delete(
      `${this.baseUrl}/political-user/${id}`,
      {
        withCredentials: true,
        responseType: 'text'
      }
    );
  }

  // ================= WORKER MANAGEMENT =================
  // getWorkers(): Observable<any[]> {
  //   return this.http.get<any[]>(
  //     `${this.baseUrl}/workers`,
  //     { withCredentials: true }
  //   );
  // }

  // deactivateWorker(id: number): Observable<any> {
  //   return this.http.put(
  //     `${this.baseUrl}/worker/${id}/deactivate`,
  //     {},
  //     {
  //       withCredentials: true,
  //       responseType: 'text'
  //     }
  //   );
  // }


  // Create Nagaradhyaksha
createNagaradhyaksha(data: FormData) {
  return this.http.post(
    `${this.baseUrl}/create-nagaradhyaksha`,
    data,
    { withCredentials: true }
  );
}

// Get all Nagaradhyaksha
getNagaradhyaksha() {
  return this.http.get<any[]>(
    `${this.baseUrl}/nagaradhyaksha`,
    { withCredentials: true }
  );
}

// Get Nagaradhyaksha by location
getNagaradhyakshaByLocation(state: string, district: string, taluka: string) {
  return this.http.get<any>(
    `${this.baseUrl}/nagaradhyaksha/location?state=${state}&district=${district}&taluka=${taluka}`,
    { withCredentials: true }
  );
}

// Delete Nagaradhyaksha
deleteNagaradhyaksha(id: number) {
  return this.http.delete(
    `${this.baseUrl}/nagaradhyaksha/${id}`,
    {
      responseType: 'text',
      withCredentials: true
    }
  );
}


// ===============================
// admin.service.ts methods
// ===============================

createNagarsevak(data: FormData) {
  return this.http.post(
    `${this.baseUrl}/create-nagarsevak`,
    data,
    { withCredentials: true }
  );
}

getNagarsevaks() {
  return this.http.get<any[]>(
    `${this.baseUrl}/nagarsevaks`,
    { withCredentials: true }
  );
}

getNagarsevakByWard(state: string, district: string, taluka: string, wardNo: number) {
  return this.http.get<any>(
    `${this.baseUrl}/nagarsevak/location?state=${state}&district=${district}&taluka=${taluka}&wardNo=${wardNo}`,
    { withCredentials: true }
  );
}

deleteNagarsevak(id: number) {
  return this.http.delete(
    `${this.baseUrl}/nagarsevak/${id}`,
    {
      responseType: 'text',
      withCredentials: true
    }
  );
}

//___________________________ WORKER MEEDNPOINTS _______________
// ===============================
// admin.service.ts methods
// ===============================

getWorkers() {
  return this.http.get<any[]>(
    `${this.baseUrl}/workers`,
    { withCredentials: true }
  );
}

getWorkerById(id: number) {
  return this.http.get<any>(
    `${this.baseUrl}/worker/${id}`,
    { withCredentials: true }
  );
}

getWorkersByType(workType: string) {
  return this.http.get<any[]>(
    `${this.baseUrl}/workers/type?workType=${workType}`,
    { withCredentials: true }
  );
}

getWorkersByLocation(state: string, district: string, taluka: string) {
  return this.http.get<any[]>(
    `${this.baseUrl}/workers/location?state=${state}&district=${district}&taluka=${taluka}`,
    { withCredentials: true }
  );
}

activateWorker(id: number) {
  return this.http.put(
    `${this.baseUrl}/worker/${id}/activate`,
    {},
    { responseType: 'text', withCredentials: true }
  );
}

deactivateWorker(id: number) {
  return this.http.put(
    `${this.baseUrl}/worker/${id}/deactivate`,
    {},
    { responseType: 'text', withCredentials: true }
  );
}

deleteWorker(id: number) {
  return this.http.delete(
    `${this.baseUrl}/worker/${id}`,
    { responseType: 'text', withCredentials: true }
  );
}


getAlerts() {
  return this.http.get<any[]>(
    `${this.baseUrl}/alerts`,
    { withCredentials: true }
  );
}


}
