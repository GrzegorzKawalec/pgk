export class RouteAppInfo {
  static readonly ROUTE: string = 'app-info';
  static readonly ROUTE_COMMANDS: string[] = [RouteAppInfo.ROUTE];
}

export class RouteSignIn {
  static readonly ROUTE: string = 'sign-in';
  static readonly ROUTE_COMMANDS: string[] = [RouteSignIn.ROUTE];
}

export class RouteUserManagement {
  static readonly ROUTE: string = 'user-management';
  static readonly ROUTE_COMMANDS: string[] = [RouteUserManagement.ROUTE];

  static readonly USERS: string = 'users';
  static readonly USERS_COMMANDS: string[] = [RouteUserManagement.ROUTE, RouteUserManagement.USERS];

  static readonly ROLES: string = 'roles';
  static readonly ROLES_COMMANDS: string[] = [RouteUserManagement.ROUTE, RouteUserManagement.ROLES];

  static readonly UPSERT: string = 'upsert';
  static readonly USERS_UPSERT_COMMANDS: string[] = [...RouteUserManagement.USERS_COMMANDS, RouteUserManagement.UPSERT];
  static readonly USERS_UPSERT_ID_PARAM: string = 'userId';
  static readonly ROLES_UPSERT_COMMANDS: string[] = [...RouteUserManagement.ROLES_COMMANDS, RouteUserManagement.UPSERT];
  static readonly ROLES_UPSERT_ID_PARAM: string = 'roleId';
}
