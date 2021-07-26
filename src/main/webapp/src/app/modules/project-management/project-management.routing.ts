import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Authority} from '../../common/api/api-models';
import {RouteProjectManagement} from '../../common/const/routes';
import {AuthGuard} from '../../core/auth/auth.guard';
import {LegalActsUpsertComponent} from './components/legal-acts/legal-acts-upsert/legal-acts-upsert.component';
import {ProjectManagementComponent} from './components/project-management.component';
import {ProjectUpsertComponent} from './components/projects/project-upsert/project-upsert.component';

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
        children: [
          {
            path: '',
            component: ProjectManagementComponent,
            canActivate: [AuthGuard]
          },
          {
            path: RouteProjectManagement.UPSERT,
            data: {authorities: [Authority.PROJECT_WRITE]},
            children: [
              {
                path: '',
                component: ProjectUpsertComponent
              },
              {
                path: ':' + RouteProjectManagement.PROJECTS_UPSERT_ID_PARAM,
                component: ProjectUpsertComponent
              }
            ]
          }
        ]
      },
      {
        path: RouteProjectManagement.LEGAL_ACTS,
        children: [
          {
            path: '',
            component: ProjectManagementComponent,
            data: {authorities: [Authority.LEGAL_ACTS_READ, Authority.LEGAL_ACTS_WRITE]},
            canActivate: [AuthGuard]
          },
          {
            path: RouteProjectManagement.UPSERT,
            data: {authorities: [Authority.LEGAL_ACTS_WRITE]},
            canActivate: [AuthGuard],
            children: [
              {
                path: '',
                component: LegalActsUpsertComponent
              },
              {
                path: ':' + RouteProjectManagement.LEGAL_ACTS_UPSERT_ID_PARAM,
                component: LegalActsUpsertComponent
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
export class ProjectManagementRouting {
}
