import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from './core/auth/auth.guard';
import {SignInComponent} from './core/auth/sign-in/sign-in.component';
import {ROUTE_APP_INFO, ROUTE_SIGN_IN} from './common/const/routes';

const routes: Routes = [
  {
    path: ROUTE_SIGN_IN,
    component: SignInComponent
  },
  {
    path: ROUTE_APP_INFO,
    loadChildren: () => import('./modules/app-info/app-info.module').then(m => m.AppInfoModule),
    canActivate: [AuthGuard]
  },
  {
    path: '',
    redirectTo: '',
    pathMatch: 'full',
    canActivate: [AuthGuard]
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
