import {NgModule} from '@angular/core';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {COMMON_MODULES} from '../../common/const/common-modules';
import {AppInfoRouting} from './app-info.routing';
import { AppInfoComponent } from './app-info/app-info.component';

@NgModule({
  declarations: [
    AppInfoComponent
  ],
  imports: [
    COMMON_MODULES,
    AppInfoRouting,
    MatProgressSpinnerModule
  ]
})
export class AppInfoModule {
}
