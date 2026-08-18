import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PostResponse } from '../dtos/postresponse';
import { FeedRequest } from '../dtos/feedrequest';
import { PageResponse } from '../dtos/pageresponse';

@Injectable({
  providedIn: 'root'
})
export class PostService {

   private baseUrl = 'http://localhost:8080'; // Gateway URL

  constructor(private http: HttpClient) {}


getFeed() {
  return this.http.get<PostResponse[]>(`${this.baseUrl}/posts/getFeed`,{withCredentials: true }
  );
}

getFeedPage(req: { page: number; size: number }){
  const params = new HttpParams()
                  .set('page',req.page.toString())
                  .set('size',req.size.toString());
  return this.http.get<PostResponse[]>(`${this.baseUrl}/posts/getFeedPage`, 
    { params: params, withCredentials: true } );
}


  // ❤️ LIKE / UNLIKE
  toggleLike(postId: number): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/posts/${postId}/like`,
      {},
      { responseType: 'text', withCredentials: true }
    );
  }

  // 💬 ADD COMMENT
  addComment(postId: number, text: string): Observable<any> {
    return this.http.post(
      `${this.baseUrl}/posts/${postId}/comments`,
      { text },
      { withCredentials: true }
    );
  }

  // 📥 GET COMMENTS
  getComments(postId: number): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/posts/${postId}/comments`,
      { withCredentials: true }
    );
  }

  // 📝 UPLOAD POST (we’ll use later)
  uploadPost(formData: FormData) {
  return this.http.post(
    `${this.baseUrl}/posts/upload`,
    formData,
    {
      withCredentials: true
    }
  );
}


getMyPosts(req: { page: number; size: number }) {

  const params = new HttpParams()
    .set('page', req.page.toString())
    .set('size', req.size.toString());

  return this.http.get<PageResponse<PostResponse>>(
    `${this.baseUrl}/posts/my-posts`,
    {
      params,
      withCredentials: true
    }
  );
}

getWardStats() {
  return this.http.get<any>(
    `${this.baseUrl}/posts/ward-stats`,
    { withCredentials: true }
  );
}

}
