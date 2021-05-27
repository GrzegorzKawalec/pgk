import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {Authority} from '../../../../common/api/api-models';
import {RouteUserManagement} from '../../../../common/const/routes';

@Component({
  selector: 'pgk-roles',
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.scss']
})
export class RolesComponent {

  readonly requiredUpsertAuthorities: Authority[] = [Authority.USER_WRITE];

  constructor(
    private router: Router
  ) {
  }

  clickAddRole(): void {
    this.router.navigate(RouteUserManagement.ROLES_UPSERT_COMMANDS);
  }

}
