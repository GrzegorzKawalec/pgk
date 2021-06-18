import {Authority} from '../api/api-models';

export class AuthorityUtil {

  static hasUnmodifiableAuthority(authorities: Authority[]): boolean {
    if (!authorities || authorities.length < 1) {
      return false;
    }
    for (let authority of authorities) {
      if (this.isUnmodifiableAuthority(authority)) {
        return true;
      }
    }
    return false;
  }

  static isUnmodifiableAuthority(authority: Authority): boolean {
    return Authority.ADMIN === authority ||
      Authority.GUEST === authority;
  }

}
