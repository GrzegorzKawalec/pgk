import {Component} from '@angular/core';
import {Authority} from '../../../../common/api/api-models';

@Component({
  selector: 'pgk-legal-acts',
  templateUrl: './legal-acts.component.html',
  styleUrls: ['./legal-acts.component.scss']
})
export class LegalActsComponent {

  readonly requiredUpsertAuthorities: Authority[] = [Authority.LEGAL_ACTS_WRITE];

  readonly prefixTranslateMessage: string = 'project-management.legal-acts.';

  clickAddLegalAct(): void {

  }

}
