import {NgModule} from '@angular/core';
import {MatTabsModule} from '@angular/material/tabs';
import {COMMON_MODULES} from '../../common/const/common-modules';
import {UserManagementRouting} from './user-management.routing';
import {UserManagementComponent} from './user-management/user-management.component';

@NgModule({
  declarations: [
    UserManagementComponent
  ],
  imports: [
    COMMON_MODULES,
    UserManagementRouting,
    MatTabsModule
  ]
})
export class UserManagementModule {
}
