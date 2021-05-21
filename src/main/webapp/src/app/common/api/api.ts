const PREFIX: string = "/api";

export class AuthAPI {
  static readonly url: string = PREFIX + '/sign';
  static readonly signIn: string = AuthAPI.url + '-in';
  static readonly signOut: string = AuthAPI.url + '-out';
}

export class InfoAPI {
  static readonly url: string = PREFIX + '/info';
  static readonly basicInfo: string = InfoAPI.url + '/basic';
}

export class UserAPI {
  static readonly url: string = PREFIX + '/user';
  static readonly me: string = UserAPI.url + '/me';
}
