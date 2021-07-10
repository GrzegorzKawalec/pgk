import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {Authority} from '../../../../common/api/api-models';
import {RouteProjectManagement} from '../../../../common/const/routes';

@Component({
  selector: 'pgk-legal-acts',
  templateUrl: './legal-acts.component.html',
  styleUrls: ['./legal-acts.component.scss']
})
export class LegalActsComponent {

  readonly requiredUpsertAuthorities: Authority[] = [Authority.LEGAL_ACTS_WRITE];

  readonly prefixTranslateMessage: string = 'project-management.legal-acts.';

  constructor(
    private router: Router
  ) {
  }

  clickAddLegalAct(): void {
    this.router.navigate(RouteProjectManagement.LEGAL_ACTS_UPSERT_COMMANDS);
  }

}
