import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Authority} from '../../common/api/api-models';
import {RouteUserManagement} from '../../common/const/routes';
import {AuthGuard} from '../../core/auth/auth.guard';
import {RoleUpsertComponent} from './user-management/roles/role-upsert/role-upsert.component';
import {UserManagementComponent} from './user-management/user-management.component';
import {UserUpsertComponent} from './user-management/users/user-upsert/user-upsert.component';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        redirectTo: 'users',
        pathMatch: 'full'
      },
      {
        path: RouteUserManagement.USERS,
        children: [
          {
            path: '',
            component: UserManagementComponent,
            data: {authorities: [Authority.USER_READ, Authority.USER_WRITE]},
            canActivate: [AuthGuard]
          },
          {
            path: RouteUserManagement.UPSERT,
            data: {authorities: [Authority.USER_WRITE]},
            children: [
              {
                path: '',
                component: UserUpsertComponent
              },
              {
                path: ':' + RouteUserManagement.USERS_UPSERT_ID_PARAM,
                component: UserUpsertComponent
              }
            ]
          }
        ]
      },
      {
        path: RouteUserManagement.ROLES,
        children: [
          {
            path: '',
            component: UserManagementComponent,
            data: {authorities: [Authority.ROLE_READ, Authority.ROLE_WRITE]},
            canActivate: [AuthGuard]
          },
          {
            path: RouteUserManagement.UPSERT,
            data: {authorities: [Authority.ROLE_WRITE]},
            canActivate: [AuthGuard],
            children: [
              {
                path: '',
                component: RoleUpsertComponent
              },
              {
                path: ':' + RouteUserManagement.ROLES_UPSERT_ID_PARAM,
                component: RoleUpsertComponent
              }
            ]
          }
        ]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserManagementRouting {
}
