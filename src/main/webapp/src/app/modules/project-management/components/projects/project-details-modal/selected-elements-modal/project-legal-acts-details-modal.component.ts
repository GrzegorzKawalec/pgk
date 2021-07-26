import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {LegalActDTO, ProjectDTO} from '../../../../../../common/api/api-models';
import {StringUtils} from '../../../../../../common/utils/string.utils';

@Component({
  template: `
    <h2 mat-dialog-title>{{prefixTranslateMessage + 'legal-acts' | translate}}</h2>
    <div mat-dialog-content fxLayout="column">
      <div *ngFor="let legalAct of legalActs" class="single-row" fxLayoutGap="2em">
        <span fxFlex="grow">{{legalAct.name}}</span>
        <div fxLayout="row">
          <span>{{legalAct.dateOfStr}}</span>
          <mat-icon svgIcon="link" class="link-icon" (click)="clickLink(legalAct)"
                    matTooltip="{{prefixTranslateMessage + 'open-legal-act-in-new-tab' | translate}}"></mat-icon>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .single-row {
      text-align: left;
    }

    .link-icon {
      height: 1.25em;
      cursor: pointer;
    }
  `]
})
export class ProjectLegalActsDetailsModalComponent {

  readonly prefixTranslateMessage: string = 'project-management.projects-upsert.';

  readonly legalActs: LegalActDTO[] = [];

  constructor(
    @Inject(MAT_DIALOG_DATA) data: ProjectDTO
  ) {
    this.legalActs = ProjectLegalActsDetailsModalComponent.sortLegalActs(data);
  }

  clickLink(legalAct: LegalActDTO): void {
    window.open(legalAct.link, '_blank');
  }

  private static sortLegalActs(project: ProjectDTO): LegalActDTO[] {
    if (!project || !project.legalActs) {
      return [];
    }
    return project.legalActs.sort((a, b) => StringUtils.compareString(a.name, b.name));
  }

}
