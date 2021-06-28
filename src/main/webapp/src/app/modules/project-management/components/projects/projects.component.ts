import {Component} from '@angular/core';
import {Authority} from '../../../../common/api/api-models';

@Component({
  selector: 'pgk-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.scss']
})
export class ProjectsComponent {

  readonly requiredUpsertAuthorities: Authority[] = [Authority.PROJECT_WRITE, Authority.LEGAL_ACTS_WRITE];

  readonly prefixTranslateMessage: string = 'project-management.projects.';

  clickAddProject(): void {

  }

}
