import { Component, HostListener } from '@angular/core';
import { PostResponse } from 'src/app/dtos/postresponse';
import { AuthService } from 'src/app/services/auth.service';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent {

  showCreatePost = false;
  selectedProfileImage!: File | null;
profilePreview: string | null = null;
isUploading = false;

  caption = '';
  selectedFile!: File | null;
  imagePreview: string | null = null;
  postType: 'POST' | 'COMPLAINT' = 'POST';


    posts: PostResponse[] = [];

  page = 0;
  size = 5;
  isLoading = false;
  isLastPage = false;
    user:any;

  userPosts: any[] = [];

  showEditProfile = false;

editForm: any = {};

openEditProfile() {
  this.showEditProfile = true;

  // pre-fill form
  this.editForm = { ...this.user };
}

closeEditProfile() {
  this.showEditProfile = false;
}

  constructor(private postService:PostService,
              private authService:AuthService
  ){}


  ngOnInit(): void {

    // ✅ get user from localStorage
    this.user = JSON.parse(localStorage.getItem('user')!);

    // ✅ load user posts (optional for now)
    this.loadUserPosts();
  }

  

    // 🚀 LOAD POSTS

loadPosts() {
  if (this.isLoading || this.isLastPage) return;

  this.isLoading = true;

  this.postService.getFeedPage({
    page: this.page,
    size: this.size
  })  
  .subscribe({
    next: (res: any[]) => {

      // ✅ append posts (no .content now)
      this.posts = [...this.posts, ...res];

      // ✅ detect last page manually
      if (res.length < this.size) {
        this.isLastPage = true;
      }

      this.page++;
      this.isLoading = false;
    },
    error: (err) => {
      console.error(err);
      this.isLoading = false;
    }
  });
}




toggleLike(post: PostResponse) {

  this.postService.toggleLike(post.id)
    .subscribe({
      next: (res) => {

        if (res === 'LIKED') {
          post.likeCount++;
          post.isLiked = true;
        } else {
          post.likeCount--;
          post.isLiked = false;
        }

      },
      error: (err) => console.error(err)
    });
}

@HostListener('window:scroll', [])
onScroll(): void {

  const threshold = 300;

  const position = window.innerHeight + window.scrollY;
  const height = document.body.offsetHeight;

  if (position >= height - threshold) {
    this.loadPosts();
  }
}

addComment(post: PostResponse, text: string) {

  if (!text || text.trim() === '') return;

  this.postService.addComment(post.id, text)
    .subscribe({
      next: () => {
        post.commentCount++;
      },
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

// 📸 IMAGE SELECT
onFileSelected(event: any) {
  const file = event.target.files[0];

  if (file) {
    this.selectedFile = file;

    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result as string;
    };
    reader.readAsDataURL(file);
  }
}

// 🔄 RESET
resetForm() {
  this.caption = '';
  this.selectedFile = null;
  this.imagePreview = null;
  this.postType = 'POST';
}

submitPost() {

  const formData = new FormData();

  formData.append('caption', this.caption);
  formData.append('type', this.postType);

  if (this.selectedFile) {
    formData.append('image', this.selectedFile);
  }

  this.postService.uploadPost(formData)
    .subscribe({
      next: () => {
        this.closeCreatePost();
        this.page = 0;
        this.posts = [];
        this.loadPosts();
      },
      error: (err) => console.error(err)
    });
}

// 📸 SELECT IMAGE
onProfileImageSelected(event: any) {
  const file = event.target.files[0];

  if (file) {
    this.selectedProfileImage = file;

    const reader = new FileReader();
    reader.onload = () => {
      this.profilePreview = reader.result as string;
    };
    reader.readAsDataURL(file);
  }
}


// 🚀 UPLOAD IMAGE
uploadProfileImage() {

  if (!this.selectedProfileImage) return;

  this.isUploading = true;

  this.authService.uploadProfileImage(this.selectedProfileImage)
    .subscribe({
      next: (imageUrl: string) => {

        // ✅ fallback safety
        if (!imageUrl) {
          console.error("No image URL returned");
          this.isUploading = false;
          return;
        }

        // ✅ update UI instantly
        this.user.profileImage = imageUrl;

        // ✅ update localStorage
        localStorage.setItem('user', JSON.stringify(this.user));

        // ✅ reset UI
        this.profilePreview = null;
        this.selectedProfileImage = null;
        this.isUploading = false;

      },
      error: (err) => {
        console.error(err);
        this.isUploading = false;
      }
    });
}

saveProfile() {

  this.authService.updateProfile(this.editForm)
    .subscribe({
      next: (res) => {

        // ✅ update UI
        this.user = res;

        // ✅ update localStorage
        localStorage.setItem('user', JSON.stringify(res));

        // 🔥 RELOAD NAGARSEVAK BASED ON NEW DATA
        this.authService.getNagarsevak().subscribe({
          next: (n) => this.authService.nagarsevak = n
        });

        this.showEditProfile = false;
      }
    });
}

loadUserPosts() {

  if (this.isLoading || this.isLastPage) return;

  this.isLoading = true;

  this.postService.getMyPosts({
    page: this.page,
    size: this.size
  })
  .subscribe({
    next: (res) => {
      console.log(res);

      this.userPosts = [...this.userPosts, ...res.content];

      this.isLastPage = res.last;
      this.page++;

      this.isLoading = false;
    },
    error: (err) => {
      console.error(err);
      this.isLoading = false;
    }
  });
}

}
