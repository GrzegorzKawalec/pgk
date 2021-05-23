import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {ROUTE_SIGN_IN} from '../../common/const/routes';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const url: string = AuthGuard.getUrl(state);
    if (AuthGuard.guardIsNotRequired(url)) {
      return true;
    }
    return this.isLoggedIn(url) && this.hasPermission(url);
  }

  private static getUrl(state: RouterStateSnapshot): string {
    let url: string = state.url;
    if (url.startsWith('/')) {
      url = url.substr(1);
    }
    return url;
  }

  private static guardIsNotRequired(url: string): boolean {
    return url.startsWith(ROUTE_SIGN_IN);
  }

  private isLoggedIn(url: string): boolean {
    if (!this.authService.loggedUser) {
      this.authService.urlBeforeRedirectToSignIn = url;
      this.router.navigate([ROUTE_SIGN_IN]);
      return false;
    }
    return true;
  }

  private hasPermission(url: string): boolean {
    return true;
  }

}
