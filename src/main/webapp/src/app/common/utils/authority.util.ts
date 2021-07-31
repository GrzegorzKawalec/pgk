import {Authority} from '../api/api-models';

export class AuthorityUtil {

  static hasUnmodifiableAuthority(authorities: Authority[], withoutEmployeeAuth: boolean = false): boolean {
    if (!authorities || authorities.length < 1) {
      return false;
    }
    for (let authority of authorities) {
      if (this.isUnmodifiableAuthority(authority, withoutEmployeeAuth)) {
        return true;
      }
    }
    return false;
  }

  static isUnmodifiableAuthority(authority: Authority, withoutEmployeeAuth: boolean = false): boolean {
    if (Authority.ADMIN === authority) {
      return true;
    }
    if (withoutEmployeeAuth) {
      return false;
    }
    return Authority.EMPLOYEE === authority;
  }

}
