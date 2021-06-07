import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatTooltipModule} from '@angular/material/tooltip';
import {COMMON_MODULES} from '../../const/common-modules';
import {AddButtonComponent} from './add-button.component';

@NgModule({
  declarations: [AddButtonComponent],
  exports: [AddButtonComponent],
  imports: [
    COMMON_MODULES,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule
  ]
})
export class AddButtonModule {
}
