import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {DateAdapter, MatNativeDateModule} from '@angular/material/core';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatDialogModule} from '@angular/material/dialog';
import {MatDividerModule} from '@angular/material/divider';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatListModule} from '@angular/material/list';
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
import {UpsertFormModule} from '../../common/components/upsert-form/upsert-form.module';
import {COMMON_MODULES} from '../../common/const/common-modules';
import {PGKDateAdapter} from '../../core/internationalization/date-adapter';
import {LegalActDescriptionModalComponent} from './components/legal-acts/legal-act-description-modal.component';
import {LegalActDetailsModalComponent} from './components/legal-acts/legal-act-details-modal/legal-act-details-modal.component';
import {LegalActsUpsertComponent} from './components/legal-acts/legal-acts-upsert/legal-acts-upsert.component';
import {LegalActsComponent} from './components/legal-acts/legal-acts.component';
import {ProjectManagementComponent} from './components/project-management.component';
import {ManageLegalActsModalComponent} from './components/projects/project-upsert/manage-legal-acts-modal/manage-legal-acts-modal.component';
import {ManageParticipantsModalComponent} from './components/projects/project-upsert/manage-participants-modal/manage-participants-modal.component';
import {OverlappingProjectModalComponent} from './components/projects/project-upsert/manage-participants-modal/overlapping-project-modal/overlapping-project-modal.component';
import {ProjectUpsertComponent} from './components/projects/project-upsert/project-upsert.component';
import {ProjectsComponent} from './components/projects/projects.component';
import {ProjectManagementRouting} from './project-management.routing';

@NgModule({
  declarations: [
    ProjectManagementComponent,

    LegalActsComponent,
    LegalActsUpsertComponent,
    LegalActDescriptionModalComponent,
    LegalActDetailsModalComponent,

    ProjectsComponent,
    ProjectUpsertComponent,
    ManageLegalActsModalComponent,
    ManageParticipantsModalComponent,
    OverlappingProjectModalComponent
  ],
  imports: [
    COMMON_MODULES,
    ProjectManagementRouting,

    AddButtonModule,
    AuditingInfoModule,
    FilterPanelModule,
    LoaderModule,
    UpsertFormModule,

    MatButtonModule,
    MatCheckboxModule,
    MatDividerModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatDialogModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatPaginatorModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatTooltipModule
  ],
  providers: [
    {provide: DateAdapter, useClass: PGKDateAdapter}
  ]
})
export class ProjectManagementModule {
}
