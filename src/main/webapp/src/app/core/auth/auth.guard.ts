import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {Authority} from '../../common/api/api-models';
import {RouteSignIn} from '../../common/const/routes';
import {AuthHelper} from './auth.helper';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  private readonly availableAuthorities: Authority[] = Object.values(Authority);

  constructor(
    private router: Router,
    private snackBar: MatSnackBar,
    private authHelper: AuthHelper,
    private authService: AuthService,
    private translateService: TranslateService
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const url: string = AuthGuard.getUrl(state);
    if (AuthGuard.guardIsNotRequired(url)) {
      return true;
    }
    if (!this.isLoggedIn(url)) {
      return false;
    }
    const hasAuthorities: boolean = this.hasAuthorities(route);
    if (!hasAuthorities) {
      const message: string = this.translateService.instant('exception.ACCESS_DENIED');
      this.snackBar.open(message, 'OK', {duration: 2500});
    }
    return hasAuthorities;
  }

  private static getUrl(state: RouterStateSnapshot): string {
    let url: string = state.url;
    if (url.startsWith('/')) {
      url = url.substr(1);
    }
    return url;
  }

  private static guardIsNotRequired(url: string): boolean {
    return url.startsWith(RouteSignIn.ROUTE);
  }

  private isLoggedIn(url: string): boolean {
    if (!this.authService.loggedUser) {
      this.authService.urlBeforeRedirectToSignIn = url;
      this.router.navigate(RouteSignIn.ROUTE_COMMANDS);
      return false;
    }
    return true;
  }

  private hasAuthorities(route: ActivatedRouteSnapshot): boolean {
    const requiredAuthorities: Authority[] = this.getRequiredAuthorities(route);
    if (!requiredAuthorities) {
      return true;
    }
    return this.authHelper.hasAuthorities(requiredAuthorities);
  }

  private getRequiredAuthorities(route: ActivatedRouteSnapshot): Authority[] {
    let routeWithData: ActivatedRouteSnapshot = route;
    while (routeWithData && !routeWithData.data.authorities) {
      routeWithData = routeWithData.parent;
    }
    const data: any = routeWithData && routeWithData.data || {};
    const rawAuthorities: any[] = data && data.authorities || [];
    const authorities: Authority[] = rawAuthorities.filter(a => this.availableAuthorities.includes(a));
    if (!authorities || authorities.length < 1) {
      return null;
    }
    return authorities;
  }

}
