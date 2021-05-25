import {Injectable} from '@angular/core';
import {Authority, UserDTO} from '../../common/api/api-models';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthHelper {

  constructor(
    private authService: AuthService
  ) {
  }

  hasAuthorities(requiredAuthorities: Authority[]): boolean {
    if (!requiredAuthorities || requiredAuthorities.length < 1) {
      return true;
    }
    const userAuthorities: Authority[] = this.getLoggedUserAuthorities();
    if (!userAuthorities) {
      return false;
    }
    return AuthHelper.hasAnyRequiredAuthorities(userAuthorities, requiredAuthorities);
  }

  private getLoggedUserAuthorities(): Authority[] {
    const loggedUser: UserDTO = this.authService.loggedUser;
    return loggedUser ? loggedUser.authorities : null;
  }

  private static hasAnyRequiredAuthorities(userAuthorities: Authority[], requiredAuthorities: Authority[]): boolean {
    if (AuthHelper.hasAdminAuthority(userAuthorities)) {
      return true;
    }
    for (let requiredAuthority of requiredAuthorities) {
      if (userAuthorities.includes(requiredAuthority)) {
        return true;
      }
    }
    return false;
  }

  private static hasAdminAuthority(userAuthorities: Authority[]): boolean {
    return userAuthorities.includes(Authority.ADMIN);
  }

}
