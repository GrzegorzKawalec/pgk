import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RouteAppInfo, RouteDashboard, RouteProjectManagement, RouteSignIn, RouteUserManagement} from './common/const/routes';
import {AuthGuard} from './core/auth/auth.guard';
import {SignInComponent} from './core/auth/sign-in/sign-in.component';

const routes: Routes = [
  {
    path: RouteSignIn.ROUTE,
    component: SignInComponent
  },
  {
    path: RouteAppInfo.ROUTE,
    loadChildren: () => import('./modules/app-info/app-info.module').then(m => m.AppInfoModule),
    canActivate: [AuthGuard]
  },
  {
    path: RouteDashboard.ROUTE,
    loadChildren: () => import('./modules/dashboard/dashboard.module').then(m => m.DashboardModule),
    canActivate: [AuthGuard]
  },
  {
    path: RouteProjectManagement.ROUTE,
    loadChildren: () => import('./modules/project-management/project-management.module').then(m => m.ProjectManagementModule),
    canActivate: [AuthGuard]
  },
  {
    path: RouteUserManagement.ROUTE,
    loadChildren: () => import('./modules/user-management/user-management.module').then(m => m.UserManagementModule),
    canActivate: [AuthGuard]
  },
  {
    path: '',
    redirectTo: RouteDashboard.ROUTE,
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: ''
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRouting {
}
