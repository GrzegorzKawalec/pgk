export class RouteAppInfo {
  static readonly ROUTE: string = 'app-info';
  static readonly ROUTE_COMMANDS: string[] = [RouteAppInfo.ROUTE];
}

export class RouteSignIn {
  static readonly ROUTE: string = 'sign-in';
  static readonly ROUTE_COMMANDS: string[] = [RouteSignIn.ROUTE];
}

export class RouteProjectManagement {
  static readonly ROUTE: string = 'project-management';
  static readonly ROUTE_COMMANDS: string[] = [RouteProjectManagement.ROUTE];

  static readonly PROJECTS: string = 'projects';
  static readonly PROJECTS_COMMANDS: string[] = [RouteProjectManagement.ROUTE, RouteProjectManagement.PROJECTS];

  static readonly LEGAL_ACTS: string = 'legal-acts';
  static readonly LEGAL_ACTS_COMMANDS: string[] = [RouteProjectManagement.ROUTE, RouteProjectManagement.LEGAL_ACTS];

  static readonly UPSERT: string = 'upsert';
  static readonly PROJECTS_UPSERT_COMMANDS: string[] = [...RouteProjectManagement.PROJECTS_COMMANDS, RouteProjectManagement.UPSERT];
  static readonly PROJECTS_UPSERT_ID_PARAM: string = 'projectId';
  static readonly LEGAL_ACTS_UPSERT_COMMANDS: string[] = [...RouteProjectManagement.LEGAL_ACTS_COMMANDS, RouteProjectManagement.UPSERT];
  static readonly LEGAL_ACTS_UPSERT_ID_PARAM: string = 'legalActsId';
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
