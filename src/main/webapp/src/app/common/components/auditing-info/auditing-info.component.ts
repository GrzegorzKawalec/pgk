import {Component, Input} from '@angular/core';
import {AuditingDTO, AuditingInfoDTO, AuditorDTO} from '../../api/api-models';
import {SYSTEM_EMAIL} from '../../const/const';

@Component({
  selector: 'pgk-auditing-info',
  templateUrl: './auditing-info.component.html',
  styleUrls: ['./auditing-info.component.scss']
})
export class AuditingInfoComponent<T> {

  private readonly defaultLayout: string = 'column';
  fxLayout: string = this.defaultLayout;

  id: string;
  version: string;

  createdOn: string;
  createdBy: string;
  lastModifiedOn: string;
  lastModifiedBy: string;
  private _auditing: AuditingDTO<T>;

  @Input() set layout(layout: string) {
    let fxLayout: string = this.defaultLayout;
    if (layout === 'row' || layout === 'column') {
      fxLayout = layout;
    }
    this.fxLayout = fxLayout;
  }

  @Input() set auditing(auditing: AuditingDTO<T>) {
    this.setInfoFieldsToUndefined();
    this._auditing = auditing;
    this.updateField();
  }

  private setInfoFieldsToUndefined(): void {
    this.id = undefined;
    this.version = undefined;

    this.createdOn = undefined;
    this.createdBy = undefined;
    this.lastModifiedOn = undefined;
    this.lastModifiedBy = undefined;

    this._auditing = undefined;
  }

  private updateField(): void {
    if (!this._auditing) {
      return;
    }
    this.updateDtoInfo();
    this.updateAuditingInfo();
  }

  private updateDtoInfo(): void {
    const dto: any = this._auditing.dto;
    if (!dto) {
      return;
    }
    this.id = dto.id;
    this.version = dto.entityVersion;
  }

  private updateAuditingInfo(): void {
    const info: AuditingInfoDTO = this._auditing.info;
    if (!info) {
      return;
    }

    const created: AuditorDTO = info.created;
    this.createdBy = AuditingInfoComponent.prepareBy(created);
    this.createdOn = AuditingInfoComponent.prepareOn(created);

    const lastModified: AuditorDTO = info.lastModified;
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
