import { Component } from '@angular/core';
import { RoutingService } from '../../services/route/routing.service';

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [],
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.css',
})
export class WelcomeComponent {
  constructor(public routingService: RoutingService) {}
}
