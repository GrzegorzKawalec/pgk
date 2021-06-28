import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {finalize} from 'rxjs/operators';
import {UserAuditingDTO, UserDTO} from '../../../../../common/api/api-models';
import {UserManagementService} from '../../../services/user-management.service';

@Component({
  templateUrl: './user-details-modal.component.html',
  styleUrls: ['./user-details-modal.component.scss']
})
export class UserDetailsModalComponent {

  loading: boolean = false;

  auditingInfo: UserAuditingDTO;

  constructor(
    @Inject(MAT_DIALOG_DATA) data: UserDTO,
    private userManagementService: UserManagementService
  ) {
    this.loadData(data);
  }

  private loadData(userDTO: UserDTO): void {
    this.loading = true;
    this.userManagementService.getAuditingInfo(userDTO.id)
      .pipe(finalize(() => this.loading = false))
      .subscribe((auditingData: UserAuditingDTO) => this.auditingInfo = auditingData);
  }

}
