import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {Authority} from '../../../../common/api/api-models';
import {RouteProjectManagement} from '../../../../common/const/routes';

@Component({
  selector: 'pgk-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.scss']
})
export class ProjectsComponent {

  readonly requiredUpsertAuthorities: Authority[] = [Authority.PROJECT_WRITE, Authority.LEGAL_ACTS_WRITE];

  readonly prefixTranslateMessage: string = 'project-management.projects.';

  constructor(
    private router: Router
  ) {
  }

  clickAddProject(): void {
    this.router.navigate(RouteProjectManagement.PROJECTS_UPSERT_COMMANDS);
  }

}
