import {NgModule} from '@angular/core';
import {DateAdapter, MatNativeDateModule} from '@angular/material/core';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatDialogModule} from '@angular/material/dialog';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatSortModule} from '@angular/material/sort';
import {MatTableModule} from '@angular/material/table';
import {MatTabsModule} from '@angular/material/tabs';
import {MatTooltipModule} from '@angular/material/tooltip';
import {AddButtonModule} from '../../common/components/add-button/add-button.module';
import {FilterPanelModule} from '../../common/components/filter-panel/filter-panel.module';
import {LoaderModule} from '../../common/components/loader/loader.module';
import {UpsertFormModule} from '../../common/components/upsert-form/upsert-form.module';
import {COMMON_MODULES} from '../../common/const/common-modules';
import {PGKDateAdapter} from '../../core/internationalization/date-adapter';
import {LegalActDescriptionModalComponent} from './components/legal-acts/legal-act-description-modal.component';
import {LegalActsUpsertComponent} from './components/legal-acts/legal-acts-upsert/legal-acts-upsert.component';
import {LegalActsComponent} from './components/legal-acts/legal-acts.component';
import {ProjectManagementComponent} from './components/project-management.component';
import {ProjectsComponent} from './components/projects/projects.component';
import {ProjectManagementRouting} from './project-management.routing';

@NgModule({
  declarations: [
    ProjectManagementComponent,

    LegalActsComponent,
    LegalActsUpsertComponent,
    LegalActDescriptionModalComponent,

    ProjectsComponent
  ],
  imports: [
    COMMON_MODULES,
    ProjectManagementRouting,

    AddButtonModule,
    FilterPanelModule,
    LoaderModule,
    UpsertFormModule,

    MatNativeDateModule,
    MatDatepickerModule,
    MatDialogModule,
    MatIconModule,
    MatInputModule,
    MatPaginatorModule,
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
