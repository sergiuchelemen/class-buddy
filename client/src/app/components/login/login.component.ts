import { Component } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { HttpClientModule } from '@angular/common/http';
import { NgOptimizedImage } from '@angular/common';
import { RoutingService } from '../../services/route/routing.service';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [HttpClientModule, NgOptimizedImage],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  constructor(private authService: AuthService) {}

  public getLoginData() {
    this.authService.performLogin();
  }
}
