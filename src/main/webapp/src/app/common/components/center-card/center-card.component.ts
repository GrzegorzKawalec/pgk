import {Component, Input} from '@angular/core';

@Component({
  selector: 'pgk-canter-card',
  template: `
    <div class="pgk-center-content-container" fxLayoutAlign="center center">
      <mat-card [class]="zElevation" [fxLayout]="fxLayout" class="center-card-container">
        <ng-content></ng-content>
      </mat-card>
    </div>
  `,
  styleUrls: ['./center-card.component.scss']
})
export class CenterCardComponent {

  zElevation: string = 'mat-elevation-z10';

  @Input() set elevation(elevationStr: string) {
    const elevation: number = +elevationStr | 0;
    let zElevation: string = 'z';
    if (!elevation || elevation < 1) {
      zElevation += '0';
    }  else if (elevation > 24) {
      zElevation += '24';
    } else {
      zElevation += elevation;
    }
    this.zElevation = 'mat-elevation-' + zElevation;
  }

  fxLayout: string = undefined;

  @Input() set layout(layout: string) {
    let fxLayout: string = undefined;
    if (layout === 'row' || layout === 'column') {
      fxLayout = layout;
    }
    this.fxLayout = fxLayout;
  }


}
