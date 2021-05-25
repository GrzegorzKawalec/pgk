import {NgModule} from '@angular/core';
import {MatCardModule} from '@angular/material/card';
import {COMMON_MODULES} from '../../const/common-modules';
import {CenterCardComponent} from './center-card.component';

@NgModule({
  declarations: [CenterCardComponent],
  exports: [CenterCardComponent],
  imports: [
    COMMON_MODULES,
    MatCardModule
  ]
})
export class CenterCardModule {
}
