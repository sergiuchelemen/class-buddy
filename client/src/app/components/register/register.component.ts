import { Component } from '@angular/core';
import { TokenService } from '../../services/auth/token.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  constructor(private tokenService: TokenService) {}

  public getAccessToken() {
    console.log(this.tokenService.getAccessToken());
  }
}
