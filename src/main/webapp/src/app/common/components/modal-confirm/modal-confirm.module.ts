import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';
import {COMMON_MODULES} from '../../const/common-modules';
import {ModalConfirmComponent} from './modal-confirm.component';

@NgModule({
  declarations: [ModalConfirmComponent],
  exports: [ModalConfirmComponent],
  imports: [
    COMMON_MODULES,
    MatButtonModule,
    MatDialogModule
  ]
})
export class ModalConfirmModule {
}
