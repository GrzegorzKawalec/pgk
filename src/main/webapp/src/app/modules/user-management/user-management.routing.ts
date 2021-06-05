import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Authority} from '../../common/api/api-models';
import {RouteUserManagement} from '../../common/const/routes';
import {AuthGuard} from '../../core/auth/auth.guard';
import {RoleUpsertComponent} from './user-management/roles/role-upsert/role-upsert.component';
import {UserManagementComponent} from './user-management/user-management.component';

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
        component: UserManagementComponent
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
            data: {
              authorities: [Authority.ROLE_WRITE],
              allRequired: true
            },
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
