import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {finalize} from 'rxjs/operators';
import {RoleAuditingDTO, RoleDTO} from '../../../../../common/api/api-models';
import {RoleService} from '../../../services/role.service';

@Component({
  templateUrl: './role-auditing-modal.component.html',
  styleUrls: ['./role-auditing-modal.component.scss']
})
export class RoleAuditingModalComponent {

  loading: boolean = false;

  auditingInfo: RoleAuditingDTO;

  constructor(
    @Inject(MAT_DIALOG_DATA) data: RoleDTO,
    private roleService: RoleService
  ) {
    this.loadData(data);
  }

  private loadData(role: RoleDTO): void {
    this.loading = true;
    this.roleService.getAuditingInfo(role.id)
      .pipe(finalize(() => this.loading = false))
      .subscribe((auditingData: RoleAuditingDTO) => {
        this.auditingInfo = auditingData;
      });
  }

}
