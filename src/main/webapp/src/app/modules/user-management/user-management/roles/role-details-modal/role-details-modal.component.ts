import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {finalize} from 'rxjs/operators';
import {RoleAuditingDTO, RoleDTO} from '../../../../../common/api/api-models';
import {AuthorityTranslateModel, AuthorityTranslateService} from '../../../../../common/services/authority-translate.service';
import {RoleService} from '../../../services/role.service';

@Component({
  templateUrl: './role-details-modal.component.html',
  styleUrls: ['./role-details-modal.component.scss']
})
export class RoleDetailsModalComponent {

  loading: boolean = false;

  auditingInfo: RoleAuditingDTO;
  authorities: AuthorityTranslateModel[] = [];

  constructor(
    @Inject(MAT_DIALOG_DATA) data: RoleDTO,
    private roleService: RoleService,
    private authorityTranslateService: AuthorityTranslateService
  ) {
    this.loadData(data);
  }

  private loadData(role: RoleDTO): void {
    this.loading = true;
    this.roleService.getAuditingInfo(role.id)
      .pipe(finalize(() => this.loading = false))
      .subscribe((auditingData: RoleAuditingDTO) => {
        this.auditingInfo = auditingData;
        this.prepareAuthorities();
      });
  }

  private prepareAuthorities(): void {
    if (!this.auditingInfo || !this.auditingInfo.dto) {
      return;
    }
    this.authorities = this.authorityTranslateService.translateAndSort(this.auditingInfo.dto.authorities);
  }

}
