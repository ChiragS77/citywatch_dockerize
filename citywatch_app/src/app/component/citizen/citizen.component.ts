import { Component } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-citizen',
  templateUrl: './citizen.component.html',
  styleUrls: ['./citizen.component.css']
})
export class CitizenComponent {


  citizens: any[] = [];

  constructor(private service:AuthService){}

ngOnInit() {
  this.loadCitizens();
}

loadCitizens() {
  this.service.getCitizens().subscribe(res => {
    this.citizens = res;
  });
}

}
