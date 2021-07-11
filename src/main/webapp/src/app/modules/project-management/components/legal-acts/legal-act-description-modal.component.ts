import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {LegalActDTO} from '../../../../common/api/api-models';

@Component({
  template: `
    <h2 mat-dialog-title>{{legalActName}}</h2>
    <div mat-dialog-content>
        {{legalActDescription}}
    </div>
  `,
})
export class LegalActDescriptionModalComponent {

  legalActName: string = '';
  legalActDescription: string = '';

  constructor(
    @Inject(MAT_DIALOG_DATA) data: LegalActDTO
  ) {
    if (data) {
      this.legalActName = data.name || '';
      this.legalActDescription = data.description || '';
    }
  }

}
