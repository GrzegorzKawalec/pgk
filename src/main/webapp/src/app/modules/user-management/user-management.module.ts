import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatChipsModule} from '@angular/material/chips';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatSelectModule} from '@angular/material/select';
import {MatSortModule} from '@angular/material/sort';
import {MatTableModule} from '@angular/material/table';
import {MatTabsModule} from '@angular/material/tabs';
import {MatTooltipModule} from '@angular/material/tooltip';
import {LoaderModule} from '../../common/components/loader/loader.module';
import {ProgressModule} from '../../common/components/progress/progress.module';
import {UpsertFormModule} from '../../common/components/upsert-form/upsert-form.module';
import {COMMON_MODULES} from '../../common/const/common-modules';
import {UserManagementRouting} from './user-management.routing';
import {RolesUpsertComponent} from './user-management/roles/roles-upsert/roles-upsert.component';
import {RolesComponent} from './user-management/roles/roles.component';
import {UserManagementComponent} from './user-management/user-management.component';

@NgModule({
  declarations: [
    UserManagementComponent,
    RolesComponent,
    RolesUpsertComponent
  ],
  imports: [
    COMMON_MODULES,
    UserManagementRouting,

    LoaderModule,
    ProgressModule,
    UpsertFormModule,

    MatButtonModule,
    MatChipsModule,
    MatIconModule,
    MatInputModule,
    MatPaginatorModule,
    MatSelectModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatTooltipModule
  ]
})
export class UserManagementModule {
}
