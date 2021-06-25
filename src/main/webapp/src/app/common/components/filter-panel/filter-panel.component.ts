import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'pgk-filter-panel',
  template: `
    <div class="pgk-content-container" fxLayout="row" fxLayoutGap="2em">
      <ng-content></ng-content>
      <button mat-icon-button class="clear-filter-button" fxFlex="25px" matTooltip="{{'common.clear' | translate}}"
              (click)="clickClearFilter()">
        <mat-icon svgIcon="clear"></mat-icon>
      </button>
    </div>
  `,
  styleUrls: ['./filter-panel.component.scss']
})
export class FilterPanelComponent {

  @Output() clickClearEvent: EventEmitter<void> = new EventEmitter();

  clickClearFilter(): void {
    this.clickClearEvent.emit();
  }

}
