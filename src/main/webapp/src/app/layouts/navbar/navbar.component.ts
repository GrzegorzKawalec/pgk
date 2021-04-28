import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'pgk-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {

  constructor(
    private router: Router
  ) { }

  clickInfo(): void {
    this.router.navigateByUrl('app-info')
  }

}
