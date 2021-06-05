import {NgModule} from '@angular/core';
import {MatInputModule} from '@angular/material/input';
import {COMMON_MODULES} from '../../const/common-modules';
import {AuditingInfoComponent} from './auditing-info.component';

@NgModule({
  declarations: [AuditingInfoComponent],
  exports: [AuditingInfoComponent],
  imports: [
    COMMON_MODULES,
    MatInputModule
  ]
})
export class AuditingInfoModule {
}
