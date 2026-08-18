export interface LoginResponse {

  message: string;

  id: number;   
  name: string;
  email: string;
  role: string;

  profileImage?: string;

  state: string;
  district: string;
  taluka: string;
  wardNo: number;

  dob: string;   // ISO string (YYYY-MM-DD)
  age: number;
}
