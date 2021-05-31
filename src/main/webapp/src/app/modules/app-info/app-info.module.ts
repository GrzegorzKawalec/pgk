import {NgModule} from '@angular/core';
import {CenterCardModule} from '../../common/components/center-card/center-card.module';
import {LoaderModule} from '../../common/components/loader/loader.module';
import {COMMON_MODULES} from '../../common/const/common-modules';
import {AppInfoRouting} from './app-info.routing';
import {AppInfoComponent} from './app-info/app-info.component';

@NgModule({
  declarations: [
    AppInfoComponent
  ],
  imports: [
    COMMON_MODULES,
    AppInfoRouting,
    CenterCardModule,
    LoaderModule
  ]
})
export class AppInfoModule {
}
