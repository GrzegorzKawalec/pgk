import {Component, OnInit} from '@angular/core';
import {MatTabChangeEvent} from '@angular/material/tabs/tab-group';
import {Router} from '@angular/router';
import {Authority} from '../../../common/api/api-models';
import {RouteUserManagement} from '../../../common/const/routes';
import {TabModel} from '../../../common/models/tab.model';
import {TabUtil} from '../../../common/utils/tab.util';
import {AuthHelper} from '../../../core/auth/auth.helper';

@Component({
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {

  tabs: UserManagementTabModel[] = []
  activeTabIndex: number;

  readonly usersTabIndex: number = UserManagementTabModel.USERS_TAB_INDEX;
  readonly rolesTabIndex: number = UserManagementTabModel.ROLES_TAB_INDEX;

  constructor(
    private router: Router,
    private authHelper: AuthHelper
  ) {
  }

  onTabChange(changeEvent: MatTabChangeEvent): void {
    TabUtil.onTabChange(this.tabs, this.router, changeEvent);
  }

  ngOnInit(): void {
    this.tabs = UserManagementTabModel.getTabs(this.authHelper);
    this.activeTabIndex = TabUtil.getActiveTabIndexFromRouter(this.tabs, this.router);
  }

}

class UserManagementTabModel extends TabModel {

  static readonly USERS_TAB_INDEX: number = 0;
  static readonly ROLES_TAB_INDEX: number = 1;

  protected get translatePrefix(): string {
    return 'user-management.';
  }

  static getTabs(authHelper: AuthHelper): UserManagementTabModel[] {
    const tabs: UserManagementTabModel[] = [];
    tabs.push(UserManagementTabModel.usersTab());
    tabs.push(UserManagementTabModel.rolesTabs());
    return TabUtil.filterAndSortTabs(tabs, authHelper);
  }

  private static usersTab(): UserManagementTabModel {
    const tab: UserManagementTabModel = new UserManagementTabModel();
    tab._index = UserManagementTabModel.USERS_TAB_INDEX;
    tab._translateKey = tab.translatePrefix + 'tab-users';
    tab._lastElementPath = RouteUserManagement.USERS;
    tab._routeCommands = RouteUserManagement.USERS_COMMANDS;
    tab._requiredAuthorities = [Authority.USER_READ, Authority.USER_WRITE, Authority.ROLE_READ, Authority.ROLE_WRITE]
    return tab;
  }

  private static rolesTabs(): UserManagementTabModel {
    const tab: UserManagementTabModel = new UserManagementTabModel();
    tab._index = UserManagementTabModel.ROLES_TAB_INDEX;
    tab._translateKey = tab.translatePrefix + 'tab-roles';
    tab._lastElementPath = RouteUserManagement.ROLES;
    tab._routeCommands = RouteUserManagement.ROLES_COMMANDS;
    tab._requiredAuthorities = [Authority.ROLE_READ, Authority.ROLE_WRITE]
    return tab;
  }

}
