import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
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
        path: 'users',
        component: UserManagementComponent
      },
      {
        path: 'roles',
        component: UserManagementComponent
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
