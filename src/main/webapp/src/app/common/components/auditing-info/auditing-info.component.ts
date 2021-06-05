import {Component, Input} from '@angular/core';
import {AuditingDTO, AuditorDTO} from '../../api/api-models';
import {SYSTEM_EMAIL} from '../../const/const';

@Component({
  selector: 'pgk-auditing-info',
  templateUrl: './auditing-info.component.html',
  styleUrls: ['./auditing-info.component.scss']
})
export class AuditingInfoComponent {

  private readonly defaultLayout: string = 'column';
  fxLayout: string = this.defaultLayout;

  @Input() set layout(layout: string) {
    let fxLayout: string = this.defaultLayout;
    if (layout === 'row' || layout === 'column') {
      fxLayout = layout;
    }
    this.fxLayout = fxLayout;
  }

  @Input() set auditing(auditing: AuditingDTO) {
    this._auditing = auditing;
    this.updateField();
  }

  createdOn: string;
  createdBy: string;
  lastModifiedOn: string;
  lastModifiedBy: string;
  private _auditing: AuditingDTO;

  private updateField(): void {
    if (!this._auditing) {
      return;
    }

    const created: AuditorDTO = this._auditing.created;
    this.createdBy = AuditingInfoComponent.prepareBy(created);
    this.createdOn = AuditingInfoComponent.prepareOn(created);

    const lastModified: AuditorDTO = this._auditing.lastModified;
    this.lastModifiedBy = AuditingInfoComponent.prepareBy(lastModified);
    this.lastModifiedOn = AuditingInfoComponent.prepareOn(lastModified);
  }

  private static prepareBy(auditor: AuditorDTO): string {
    if (!auditor) {
      return '';
    }
    const email: string = auditor.email || '';
    if (email === SYSTEM_EMAIL) {
      return 'System';
    }
    const firstName: string = auditor.firstName || '';
    const lastName: string = auditor.lastName || '';
    return firstName + ' ' + lastName + ' (' + email + ')';
  }

  private static prepareOn(auditor: AuditorDTO): string {
    if (!auditor) {
      return '';
    }
    const date: string = auditor.date || '';
    const time: string = auditor.time || '';
    return date + ', ' + time;
  }

}
