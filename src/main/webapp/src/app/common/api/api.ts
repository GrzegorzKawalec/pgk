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
  static readonly all: string = RoleAPI.url + '/all';
  static readonly allAvailable: string = RoleAPI.url + '/available';
  static readonly auditingInfo: string = RoleAPI.url + '/auditing-info';
  static readonly existsName: string = RoleAPI.url + '/exists-name';
  static readonly existsNameParamName: string = 'name';
  static readonly existsNameParamId: string = 'excludedId';
  static readonly find: string = RoleAPI.url + '/find'
}

export class UserAPI {
  static readonly url: string = PREFIX + '/user';
  static readonly me: string = UserAPI.url + '/me';
  static readonly activate: string = UserAPI.url + '/activate';
  static readonly auditingInfo: string = UserAPI.url + '/auditing-info';
  static readonly changePassword: string = UserAPI.url + '/change-password';
  static readonly deactivate: string = UserAPI.url + '/deactivate';
  static readonly existsEmail: string = UserAPI.url + '/exists-email';
  static readonly existsEmailParamEmail: string = 'email';
  static readonly existsEmailParamId: string = 'excludedId';
  static readonly find: string = UserAPI.url + '/find'
  static readonly findUserUpsert: string = UserAPI.url + '/find-upsert'
}

// ---   account   ---
// ---     END     ---
