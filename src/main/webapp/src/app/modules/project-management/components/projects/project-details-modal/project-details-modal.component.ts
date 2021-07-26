import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog} from '@angular/material/dialog';
import {finalize} from 'rxjs/operators';
import {Authority, ProjectAuditingDTO, ProjectDTO} from '../../../../../common/api/api-models';
import {AuthHelper} from '../../../../../core/auth/auth.helper';
import {ProjectService} from '../../../services/project.service';
import {ProjectLegalActsDetailsModalComponent} from './selected-elements-modal/project-legal-acts-details-modal.component';
import {ProjectParticipantsDetailsModalComponent} from './selected-elements-modal/project-participants-details-modal.component';

@Component({
  templateUrl: './project-details-modal.component.html',
  styleUrls: ['./project-details-modal.component.scss']
})
export class ProjectDetailsModalComponent {

  private readonly requiredReadDetailsAuthorities: Authority[] = [Authority.PROJECT_WRITE, Authority.PROJECT_READ, Authority.LEGAL_ACTS_WRITE];

  readonly prefixTranslateMessage: string = 'project-management.projects-upsert.';

  loading: boolean = false;

  project: ProjectDTO;
  auditingInfo: ProjectAuditingDTO;
  readonly withAuditingDetails: boolean = false;

  constructor(
    private dialog: MatDialog,
    private projectService: ProjectService,
    @Inject(MAT_DIALOG_DATA) data: ProjectDTO,
    authHelper: AuthHelper
  ) {
    this.withAuditingDetails = authHelper.hasAuthorities(this.requiredReadDetailsAuthorities);
    if (this.withAuditingDetails) {
      this.loadData(data);
    } else {
      this.project = data;
    }
  }

  clickShowParticipants(): void {
    this.dialog.open(ProjectParticipantsDetailsModalComponent, {data: this.project});
  }

  clickShowLegalActs(): void {
    this.dialog.open(ProjectLegalActsDetailsModalComponent, {data: this.project});
  }

  getProjectManagerName(): string {
    if (!this.project || !this.project.projectManager) {
      return '';
    }
    return this.project.projectManager.lastName + ' ' + this.project.projectManager.firstName;
  }

  private loadData(project: ProjectDTO): void {
    this.loading = true;
    this.projectService.getAuditingInfo(project.id)
      .pipe(finalize(() => this.loading = false))
      .subscribe((auditingData: ProjectAuditingDTO) => {
        this.auditingInfo = auditingData;
        this.project = this.auditingInfo.dto;
      });
  }

}
