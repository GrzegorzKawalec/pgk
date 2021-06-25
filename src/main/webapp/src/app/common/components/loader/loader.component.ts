import {Component, Input} from '@angular/core';

@Component({
  selector: 'pgk-loader',
  template: `
    <div *ngIf="centered; else onlySpinner" class="pgk-center-content-container" fxLayout="column" fxLayoutAlign="center center">
      <mat-spinner></mat-spinner>
    </div>
    <ng-template #onlySpinner>
      <mat-spinner></mat-spinner>
    </ng-template>
  `
})
export class LoaderComponent {

  @Input() centered: boolean = true;

}
