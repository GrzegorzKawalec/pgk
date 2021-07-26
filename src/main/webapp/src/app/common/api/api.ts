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

export class LegalActAPI {
  static readonly url: string = PREFIX + '/legal-act';
  static readonly activate: string = LegalActAPI.url + '/activate';
  static readonly all: string = LegalActAPI.url + '/all';
  static readonly auditingInfo: string = LegalActAPI.url + '/auditing-info';
  static readonly deactivate: string = LegalActAPI.url + '/deactivate';
  static readonly find: string = LegalActAPI.url + '/find';
}

export class ProjectAPI {
  static readonly url: string = PREFIX + '/project';
  static readonly activate: string = ProjectAPI.url + '/activate';
  static readonly auditingInfo: string = ProjectAPI.url + '/auditing-info';
  static readonly dataForUpsert: string = ProjectAPI.url + '/data-for-upsert';
  static readonly deactivate: string = ProjectAPI.url + '/deactivate';
  static readonly find: string = ProjectAPI.url + '/find';
  static readonly participants: string = ProjectAPI.url + '/participants';
  static readonly selectLegalActs: string = ProjectAPI.url + '/select-legal-acts';
  static readonly selectParticipants: string = ProjectAPI.url + '/select-participants';
}

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
