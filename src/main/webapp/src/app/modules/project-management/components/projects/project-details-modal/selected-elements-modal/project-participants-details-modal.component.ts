import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {ProjectDTO, UserDTO} from '../../../../../../common/api/api-models';
import {ProjectHelper} from '../../project-helper';

@Component({
  template: `
    <h2 mat-dialog-title>{{prefixTranslateMessage + 'participants' | translate}}</h2>
    <div mat-dialog-content fxLayout="column">
      <div *ngFor="let participant of participants" class="single-row" fxLayoutGap="2em">
        <span fxFlex="grow">{{participant.lastName}} {{participant.firstName}}</span>
        <span>{{participant.email}}</span>
      </div>
    </div>
  `,
  styles: [`
    .single-row {
      text-align: left;
    }
  `]
})
export class ProjectParticipantsDetailsModalComponent {

  readonly prefixTranslateMessage: string = 'project-management.projects-upsert.';

  readonly participants: UserDTO[] = [];

  constructor(
    @Inject(MAT_DIALOG_DATA) data: ProjectDTO
  ) {
    this.participants = ProjectParticipantsDetailsModalComponent.sortParticipants(data);
  }

  private static sortParticipants(project: ProjectDTO): UserDTO[] {
    if (!project || !project.participants) {
      return [];
    }
    return project.participants.sort((a, b) => ProjectHelper.compareUser(a, b));
  }

}
