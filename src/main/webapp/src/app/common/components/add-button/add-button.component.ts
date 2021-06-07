import {Component, EventEmitter, Input, Output} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Authority} from '../../api/api-models';

@Component({
  selector: 'pgk-add-button',
  template: `
    <button *ngIf="requiredAuthorities | checkAccess" (click)="clickButton()"
            mat-fab color="primary" class="add-bottom"
            [matTooltip]="tooltipText" matTooltipPosition="before" [matTooltipDisabled]="!tooltipText">
      <mat-icon [svgIcon]="svgIcon"></mat-icon>
    </button>
  `,
  styles: [`
    .add-bottom {
      position: fixed !important;
      right: 15px;
      bottom: 15px;
    }
  `]
})
export class AddButtonComponent {

  private readonly defaultSvgIcon: string = 'add';
  svgIcon: string = this.defaultSvgIcon;

  tooltipText: string;

  @Output() clickEvent: EventEmitter<void> = new EventEmitter();

  @Input() requiredAuthorities: Authority[] = [];


  @Input() set tooltip(tooltip: string) {
    this.tooltipText = tooltip;
  }

  @Input() set tooltipTranslateKey(translateKey: string) {
    if (!translateKey) {
      this.tooltipText = null;
    } else {
      const translate: string = this.translateService.instant(translateKey);
      this.tooltipText = translate === translateKey ? null : translate;
    }
  }

  @Input() set svg(svg: string) {
    this.svgIcon = !svg ? this.defaultSvgIcon : svg;
  }

  constructor(
    private translateService: TranslateService
  ) {
  }

  clickButton(): void {
    this.clickEvent.emit();
  }

}
