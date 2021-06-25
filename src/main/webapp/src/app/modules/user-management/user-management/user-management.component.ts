import {Component, OnInit} from '@angular/core';
import {MatTabChangeEvent} from '@angular/material/tabs/tab-group';
import {Router} from '@angular/router';
import {Authority} from '../../../common/api/api-models';
import {BaseComponent} from '../../../common/components/base.component';
import {RouteUserManagement} from '../../../common/const/routes';
import {AuthHelper} from '../../../core/auth/auth.helper';

@Component({
  templateUrl: 'user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent extends BaseComponent implements OnInit {

  tabs: TabModel[] = []
  activeTabIndex: number;

  readonly usersTabIndex: number = TabModel.USER_TAB_INDEX;
  readonly rolesTabIndex: number = TabModel.ROLES_TAB_INDEX;

  constructor(
    private router: Router,
    private authHelper: AuthHelper
  ) {
    super();
  }

  onTabChange(changeEvent: MatTabChangeEvent): void {
    const tab: TabModel = this.tabs.find(tab => tab.index === changeEvent.index);
    if (tab) {
      const url: string = '/' + tab.routeCommands.join('/');
      this.router.navigateByUrl(url);
    }
  }

  ngOnInit(): void {
    this.initTab();
    this.changeActiveTabAfterInit();
  }

  private initTab(): void {
    const tabs: TabModel[] = TabModel.getTabs();
    this.tabs = tabs.filter(t => this.authHelper.hasAuthorities(t.requiredAuthorities)) || [];
  }

  private changeActiveTabAfterInit(): void {
    const urlComponents: string[] = this.router.url.split('/');
    const lastIndex: number = urlComponents ? urlComponents.length - 1 : null;
    const lastElementPath: string = lastIndex ? urlComponents[lastIndex] : null;
    const tab: TabModel = lastElementPath ? this.tabs.find(tab => tab.lastElementPath === lastElementPath) : null;
    this.activeTabIndex = tab ? tab.index : 0;
  }

}

class TabModel {

  static readonly USER_TAB_INDEX: number = 0;
  static readonly ROLES_TAB_INDEX: number = 1;

  private static readonly TRANSLATE_PREFIX: string = 'user-management.';

  private _index: number;
  private _translateKey: string;
  private _lastElementPath: string;
  private _routeCommands: string[];
  private _requiredAuthorities: Authority[];

  static getTabs(): TabModel[] {
    const tabs: TabModel[] = [];
    tabs.push(TabModel.usersTab());
    tabs.push(TabModel.roleTabs());
    return tabs.sort((t1, t2) => t1.index - t2.index);
  }

  private static usersTab(): TabModel {
    const tab: TabModel = new TabModel();
    tab._index = TabModel.USER_TAB_INDEX;
    tab._translateKey = TabModel.TRANSLATE_PREFIX + 'tab-users';
    tab._lastElementPath = RouteUserManagement.USERS;
    tab._routeCommands = RouteUserManagement.USERS_COMMANDS;
    return tab;
  }

  private static roleTabs(): TabModel {
    const tab: TabModel = new TabModel();
    tab._index = TabModel.ROLES_TAB_INDEX;
    tab._translateKey = TabModel.TRANSLATE_PREFIX + 'tab-roles';
    tab._lastElementPath = RouteUserManagement.ROLES;
    tab._routeCommands = RouteUserManagement.ROLES_COMMANDS;
    tab._requiredAuthorities = [Authority.ROLE_READ, Authority.ROLE_WRITE]
    return tab;
  }

  get index(): number {
    return this._index;
  }

  get translateKey(): string {
    return this._translateKey;
  }

  get lastElementPath(): string {
    return this._lastElementPath;
  }

  get routeCommands(): string[] {
    return this._routeCommands;
  }

  get requiredAuthorities(): Authority[] {
    return this._requiredAuthorities;
  }

}
