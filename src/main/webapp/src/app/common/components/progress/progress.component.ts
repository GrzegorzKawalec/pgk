import {Component, Input} from '@angular/core';
import {ProgressBarMode} from '@angular/material/progress-bar';

@Component({
  selector: 'pgk-progress',
  template: `
    <mat-progress-bar [mode]="mode" [ngStyle]="!visibility && {'visibility': 'hidden'}"></mat-progress-bar>
  `
})
export class ProgressComponent {

  @Input() visibility: boolean = true;
  @Input() mode: ProgressBarMode = 'indeterminate';

}
