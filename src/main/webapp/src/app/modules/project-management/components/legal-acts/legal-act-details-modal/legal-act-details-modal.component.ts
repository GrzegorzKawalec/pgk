import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {finalize} from 'rxjs/operators';
import {LegalActAuditingDTO, LegalActDTO} from '../../../../../common/api/api-models';
import {LegalActService} from '../../../services/legal-act.service';

@Component({
  templateUrl: './legal-act-details-modal.component.html',
  styleUrls: ['./legal-act-details-modal.component.scss']
})
export class LegalActDetailsModalComponent {

  readonly prefixTranslateMessage: string = 'project-management.legal-acts-upsert.';

  loading: boolean = false;
  auditingInfo: LegalActAuditingDTO;

  constructor(
    @Inject(MAT_DIALOG_DATA) data: LegalActDTO,
    private legalActService: LegalActService
  ) {
    this.loadData(data);
  }

  private loadData(legalActDTO: LegalActDTO): void {
    this.loading = true;
    this.legalActService.getAuditingInfo(legalActDTO.id)
      .pipe(finalize(() => this.loading = false))
      .subscribe((auditingData: LegalActAuditingDTO) => this.auditingInfo = auditingData);
  }

  clickLink(): void {
    window.open(this.auditingInfo.dto.link, '_blank');
  }

}
