import {Component, Input} from '@angular/core';
import {ProgressBarMode} from '@angular/material/progress-bar';

@Component({
  selector: 'pgk-progress',
  template: `
    <mat-progress-bar [mode]="mode" style="margin-bottom: -4px"></mat-progress-bar>
  `
})
export class ProgressComponent {

  @Input() mode: ProgressBarMode = 'indeterminate';

}
