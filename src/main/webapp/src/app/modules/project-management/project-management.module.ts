import {NgModule} from '@angular/core';
import {MatTabsModule} from '@angular/material/tabs';
import {AddButtonModule} from '../../common/components/add-button/add-button.module';
import {COMMON_MODULES} from '../../common/const/common-modules';
import {LegalActsComponent} from './components/legal-acts/legal-acts.component';
import {ProjectManagementComponent} from './components/project-management.component';
import {ProjectsComponent} from './components/projects/projects.component';
import {ProjectManagementRouting} from './project-management.routing';

@NgModule({
  declarations:[
    ProjectManagementComponent,

    LegalActsComponent,
    ProjectsComponent
  ],
  imports: [
    COMMON_MODULES,
    ProjectManagementRouting,

    AddButtonModule,

    MatTabsModule
  ]
})
export class ProjectManagementModule {
}
