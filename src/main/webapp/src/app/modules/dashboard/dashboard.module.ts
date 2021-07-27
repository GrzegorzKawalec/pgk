import {NgModule} from '@angular/core';
import {MatCardModule} from '@angular/material/card';
import {MatRippleModule} from '@angular/material/core';
import {MatDividerModule} from '@angular/material/divider';
import {MatIconModule} from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import {MatTooltipModule} from '@angular/material/tooltip';
import {CenterCardModule} from '../../common/components/center-card/center-card.module';
import {LoaderModule} from '../../common/components/loader/loader.module';
import {COMMON_MODULES} from '../../common/const/common-modules';
import {DashboardComponent} from './components/dashboard.component';
import {DashboardRouting} from './dashboard.routing';

@NgModule({
  declarations: [
    DashboardComponent
  ],
  imports: [
    COMMON_MODULES,
    DashboardRouting,

    CenterCardModule,
    LoaderModule,

    MatCardModule,
    MatRippleModule,
    MatDividerModule,
    MatIconModule,
    MatListModule,
    MatTooltipModule
  ]
})
export class DashboardModule {
}
