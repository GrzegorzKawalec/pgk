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

// ---    START    ---
// ---   account   ---

export class RoleAPI {
  static readonly url: string = PREFIX + '/role';
  static readonly allAvailable: string = RoleAPI.url + '/available';
  static readonly auditingInfo: string = RoleAPI.url + '/auditing-info';
  static readonly existsName: string = RoleAPI.url + '/exists-name';
  static readonly existsNameParamName: string = 'name';
  static readonly existsNameParamId: string = 'id';
  static readonly find: string = RoleAPI.url + '/find'
}

export class UserAPI {
  static readonly url: string = PREFIX + '/user';
  static readonly me: string = UserAPI.url + '/me';
  static readonly existsEmail: string = UserAPI.url + '/exists-email';
  static readonly existsEmailParamEmail: string = 'email';
  static readonly existsEmailParamId: string = 'id';
  static readonly findUserUpsert: string = UserAPI.url + '/find-upsert'
}

// ---   account   ---
// ---     END     ---
