import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {COMMON_MODULES} from '../../const/common-modules';
import {CenterCardModule} from '../center-card/center-card.module';
import {ProgressModule} from '../progress/progress.module';
import {UpsertFormComponent} from './upsert-form.component';

@NgModule({
  declarations: [UpsertFormComponent],
  exports: [UpsertFormComponent],
  imports: [
    COMMON_MODULES,
    CenterCardModule,
    ProgressModule,
    MatButtonModule
  ]
})
export class UpsertFormModule {
}
