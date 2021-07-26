import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {takeUntil} from 'rxjs/operators';
import {UserDTO} from '../../common/api/api-models';
import {BaseComponent} from '../../common/components/base.component';
import {RouteAppInfo, RouteProjectManagement, RouteUserManagement} from '../../common/const/routes';
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

  clickProjectManagement(): void {
    this.router.navigate(RouteProjectManagement.ROUTE_COMMANDS);
  }

  clickUserManagement(): void {
    this.router.navigate(RouteUserManagement.ROUTE_COMMANDS);
  }

  clickInfo(): void {
    this.router.navigate(RouteAppInfo.ROUTE_COMMANDS);
  }

  clickSignOut(): void {
    this.authService.signOut();
  }

}
