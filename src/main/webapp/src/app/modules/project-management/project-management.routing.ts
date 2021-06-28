import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RouteProjectManagement} from '../../common/const/routes';
import {AuthGuard} from '../../core/auth/auth.guard';
import {ProjectManagementComponent} from './components/project-management.component';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        redirectTo: RouteProjectManagement.PROJECTS,
        pathMatch: 'full'
      },
      {
        path: RouteProjectManagement.PROJECTS,
        component: ProjectManagementComponent,
        canActivate: [AuthGuard]
      },
      {
        path: RouteProjectManagement.LEGAL_ACTS,
        component: ProjectManagementComponent,
        canActivate: [AuthGuard]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProjectManagementRouting {
}
