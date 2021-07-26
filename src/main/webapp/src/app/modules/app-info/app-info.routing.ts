import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AppInfoComponent} from './components/app-info.component';

const routes: Routes = [
  {
    path: '',
    component: AppInfoComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AppInfoRouting {
}
