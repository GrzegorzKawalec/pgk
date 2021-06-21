import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatChipsModule} from '@angular/material/chips';
import {MatDialogModule} from '@angular/material/dialog';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatSelectModule} from '@angular/material/select';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatSortModule} from '@angular/material/sort';
import {MatTableModule} from '@angular/material/table';
import {MatTabsModule} from '@angular/material/tabs';
import {MatTooltipModule} from '@angular/material/tooltip';
import {AddButtonModule} from '../../common/components/add-button/add-button.module';
import {AuditingInfoModule} from '../../common/components/auditing-info/auditing-info.module';
import {FilterPanelModule} from '../../common/components/filter-panel/filter-panel.module';
import {LoaderModule} from '../../common/components/loader/loader.module';
import {ModalConfirmModule} from '../../common/components/modal-confirm/modal-confirm.module';
import {ProgressModule} from '../../common/components/progress/progress.module';
import {UpsertFormModule} from '../../common/components/upsert-form/upsert-form.module';
import {COMMON_MODULES} from '../../common/const/common-modules';
import {UserManagementRouting} from './user-management.routing';
import {RoleDetailsModalComponent} from './user-management/roles/role-details-modal/role-details-modal.component';
import {RoleUpsertComponent} from './user-management/roles/role-upsert/role-upsert.component';
import {RolesComponent} from './user-management/roles/roles.component';
import {UserManagementComponent} from './user-management/user-management.component';
import {UserDetailsModalComponent} from './user-management/users/user-details-modal/user-details-modal.component';
import {UserUpsertComponent} from './user-management/users/user-upsert/user-upsert.component';
import {UsersComponent} from './user-management/users/users.component';

@NgModule({
  declarations: [
    UserManagementComponent,

    RolesComponent,
    RoleUpsertComponent,
    RoleDetailsModalComponent,

    UsersComponent,
    UserUpsertComponent,
    UserDetailsModalComponent
  ],
  imports: [
    COMMON_MODULES,
    UserManagementRouting,

    AddButtonModule,
    AuditingInfoModule,
    FilterPanelModule,
    LoaderModule,
    ModalConfirmModule,
    ProgressModule,
    UpsertFormModule,

    MatButtonModule,
    MatChipsModule,
    MatDialogModule,
    MatIconModule,
    MatInputModule,
    MatPaginatorModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatTooltipModule
  ]
})
export class UserManagementModule {
}
