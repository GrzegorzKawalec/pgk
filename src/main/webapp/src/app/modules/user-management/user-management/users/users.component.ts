import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {Authority} from '../../../../common/api/api-models';
import {RouteUserManagement} from '../../../../common/const/routes';

@Component({
  selector: 'pgk-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent {

  readonly requiredUpsertAuthorities: Authority[] = [Authority.ROLE_WRITE];

  constructor(
    private router: Router
  ) {
  }

  clickAddUser(): void {
    this.router.navigate(RouteUserManagement.USERS_UPSERT_COMMANDS);
  }

}
