import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {StringUtils} from '../../../../../../../common/utils/string.utils';
import {ParticipantModel, ProjectModel} from '../manage-participants-modal.component';

@Component({
  templateUrl: './overlapping-project-modal.component.html',
  styleUrls: ['./overlapping-project-modal.component.scss']
})
export class OverlappingProjectModalComponent {

  readonly prefixTranslateMessage: string = 'project-management.projects-upsert.';

  readonly projects: ProjectModel[] = [];

  constructor(
    @Inject(MAT_DIALOG_DATA) data: ParticipantModel
  ) {
    this.prepareVisibleProjects(data);
  }

  private prepareVisibleProjects(data: ParticipantModel): void {
    if (!data || !data.projects) {
      return;
    }
    data.projects.forEach(project => {
      if (project.hasOverlappingDates) {
        this.projects.push(project);
      }
    });
    this.projects.sort((a, b) => StringUtils.compareString(a.name, b.name));
  }

}
