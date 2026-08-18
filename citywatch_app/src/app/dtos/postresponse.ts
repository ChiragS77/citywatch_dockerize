export interface PostResponse {
workType: any;
assignedTo: any;
priority: any;
wardNo: any;
  id: number;

  caption: string;

  imageUrl?: string;

  // 🔀 POST or COMPLAINT
  type: 'POST' | 'COMPLAINT';

  // 📊 Only for complaints
  status?: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED';

  username: string;

  createdAt: string; // ISO date string

  // ❤️ Aggregated
  likeCount: number;
  commentCount: number;

  // 👤 Personalization
  isLiked: boolean;
}