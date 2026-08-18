export interface UserProfile {
  name: string;
  email: string;

  role: 'USER' | 'NAGARSEVAK' | 'ADMIN';

  wardNo: number;
  taluka: string;
  district: string;

  profileImage?: string;
  partyName?: string;
}