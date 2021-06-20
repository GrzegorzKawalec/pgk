import {Authority} from '../api/api-models';

export class AuthorityUtil {

  static hasUnmodifiableAuthority(authorities: Authority[], withoutGuestAuth: boolean = false): boolean {
    if (!authorities || authorities.length < 1) {
      return false;
    }
    for (let authority of authorities) {
      if (this.isUnmodifiableAuthority(authority, withoutGuestAuth)) {
        return true;
      }
    }
    return false;
  }

  static isUnmodifiableAuthority(authority: Authority, withoutGuestAuth: boolean = false): boolean {
    if (Authority.ADMIN === authority) {
      return true;
    }
    if (withoutGuestAuth) {
      return false;
    }
    return Authority.GUEST === authority;
  }

}
