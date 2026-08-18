import { Component, HostListener, OnInit } from '@angular/core';
import { PostResponse } from 'src/app/dtos/postresponse';
import { UserProfile } from 'src/app/dtos/userprofile';
import { AuthService } from 'src/app/services/auth.service';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  workType: string = '';
  showCreatePost = false;
  isUploading = false;

  stats = {
  pending: 0,
  inProgress: 0,
  resolved: 0
};

  caption = '';
  selectedFile!: File | null;
  imagePreview: string | null = null;
  postType: 'POST' | 'COMPLAINT' = 'POST';

  posts: PostResponse[] = [];
  isLoading = false;

  user: any;
  userProfile: any;

  constructor(
    public postService: PostService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.user = JSON.parse(localStorage.getItem('user')!);
    this.loadPosts();
    this.loadWardStats();
    this.userProfile = this.authService.getNagarsevak();
  }

  loadPosts() {
    this.isLoading = true;

    this.postService.getFeed().subscribe({
      next: (res: PostResponse[]) => {
        this.posts = res;
        this.isLoading = false;
      },
      error: (err) => {
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  toggleLike(post: PostResponse) {
    this.postService.toggleLike(post.id).subscribe({
      next: (res: any) => {
        post.likeCount = res.likeCount;
        post.isLiked = res.status === 'LIKED';
      },
      error: (err) => console.error(err)
    });
  }

  addComment(post: PostResponse, text: string) {
    if (!text || text.trim() === '') return;

    this.postService.addComment(post.id, text).subscribe({
      next: () => post.commentCount++,
      error: (err) => console.error(err)
    });
  }

  openCreatePost() {
    this.showCreatePost = true;
  }

  closeCreatePost() {
    this.showCreatePost = false;
    this.resetForm();
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();

    reader.onload = (e: any) => {
      const img = new Image();
      img.src = e.target.result;

      img.onload = () => {
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');

        const MAX_WIDTH = 1024;
        const scaleSize = MAX_WIDTH / img.width;

        canvas.width = MAX_WIDTH;
        canvas.height = img.height * scaleSize;

        ctx?.drawImage(img, 0, 0, canvas.width, canvas.height);

        canvas.toBlob((blob: any) => {
          this.selectedFile = new File([blob], file.name, {
            type: 'image/jpeg',
            lastModified: Date.now()
          });

          this.imagePreview = URL.createObjectURL(blob);
        }, 'image/jpeg', 0.7);
      };
    };

    reader.readAsDataURL(file);
  }

  resetForm() {
    this.caption = '';
    this.selectedFile = null;
    this.imagePreview = null;
    this.postType = 'POST';
    this.workType = '';
  }

  submitPost() {
    if (this.postType === 'COMPLAINT' && !this.workType) {
      alert("Please select complaint type");
      return;
    }

    this.isUploading = true;

    const formData = new FormData();
    formData.append('caption', this.caption);
    formData.append('type', this.postType);

    if (this.postType === 'COMPLAINT') {
      formData.append('workType', this.workType);
    }

    if (this.selectedFile) {
      formData.append('image', this.selectedFile);
    }

    this.postService.uploadPost(formData).subscribe({
      next: () => {
        console.log("CLose the popup....");
        this.isUploading = false;
        this.closeCreatePost();
        this.loadPosts();
      },
      error: (err) => {
        this.isUploading = false;
        console.error(err);
      }
    });
  }

  //---------- Load wards stats --------------
  loadWardStats() {
  this.postService.getWardStats().subscribe({
    next: (res: any) => {
      this.stats.pending = res.pending || 0;
      this.stats.inProgress = res.inProgress || 0;
      this.stats.resolved = res.resolved || 0;
    },
    error: (err) => {
      console.error(err);
    }
  });
}

logout(){
  console.log("CLICK.....");
  this.authService.logout();
}

}
