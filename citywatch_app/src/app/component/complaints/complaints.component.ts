import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { PostResponse } from 'src/app/dtos/postresponse';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-complaints',
  templateUrl: './complaints.component.html',
  styleUrls: ['./complaints.component.css']
})
export class ComplaintsComponent implements OnInit{

  stats = {
  total: 0,
  pending: 0,
  resolved: 0
};


  page = 0;
  size = 10;
  

  constructor(private service:PostService){}

  ngOnInit(): void {
      this.loadComplaints();
  }

  
  complaints: PostResponse[] = [];
status: string = '';

loadComplaints() {
    this.service.getFeedPage({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (res) => {
        this.complaints = res;
        console.log("Complaints loaded:", res);
      },
      error: (err) => {
        console.error("Error loading complaints", err);
      }
    });
  }

assign(id: number) {
  // call assign API
}

complete(id: number) {
  // call update status API
}
}
