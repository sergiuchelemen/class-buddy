import { Component } from '@angular/core';
import { TokenService } from '../../services/auth/token.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
  constructor(private http: HttpClient, private tokenService: TokenService) {}

  public getHomeData() {
    const headers = {
      Authorization: 'Bearer ' + this.tokenService.getAccessToken(),
    };
    this.http.get<any>('http://localhost:8080/home', { headers }).subscribe({
      next: (data) => {
        console.log(data);
      },
      error: (error) => {
        console.error('There was an error!', error);
      },
    });
  }
}
