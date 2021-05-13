import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {takeUntil} from 'rxjs/operators';
import {BaseComponent} from '../../common/components/base.component';
import {UserDTO} from '../../core/api/api-models';
import {AuthService} from '../../core/auth/auth.service';

@Component({
  selector: 'pgk-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent extends BaseComponent implements OnInit {

  loggedUser: UserDTO;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
    super();
  }

  ngOnInit(): void {
    this.authService.loggedUser$
      .pipe(takeUntil(this.destroy$))
      .subscribe((user: UserDTO) => this.loggedUser = user);
  }

  clickInfo(): void {
    this.router.navigateByUrl('app-info');
  }

  clickSignOut(): void {
    this.authService.signOut();
  }

}
