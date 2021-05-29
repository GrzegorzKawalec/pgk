import {NgModule} from '@angular/core';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {COMMON_MODULES} from '../../const/common-modules';
import {ProgressComponent} from './progress.component';

@NgModule({
  declarations: [ProgressComponent],
  exports: [ProgressComponent],
  imports: [
    COMMON_MODULES,
    MatProgressBarModule
  ]
})
export class ProgressModule {
}
