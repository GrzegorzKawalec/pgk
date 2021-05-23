import {Location} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {MatTabChangeEvent} from '@angular/material/tabs/tab-group';
import {Router} from '@angular/router';
import {BaseComponent} from '../../../common/components/base.component';
import {RouteUserManagement} from '../../../common/const/routes';

@Component({
  templateUrl: 'user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent extends BaseComponent implements OnInit {

  tabs: TabModel[] = TabModel.getTabs();
  activeTabIndex: number;

  constructor(
    private router: Router,
    private location: Location
  ) {
    super();
  }

  onTabChange(changeEvent: MatTabChangeEvent): void {
    const tab: TabModel = this.tabs.find(tab => tab.index === changeEvent.index);
    if (tab) {
      const url: string = '/' + tab.routeCommands.join('/');
      this.location.go(url);
    }
  }

  ngOnInit(): void {
    this.changeActiveTabAfterInit();
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

  private static readonly TRANSLATE_PREFIX: string = 'user-management.';

  private _index: number;
  private _translateKey: string;
  private _lastElementPath: string;
  private _routeCommands: string[];

  static getTabs(): TabModel[] {
    const tabs: TabModel[] = [];
    tabs.push(TabModel.usersTab());
    tabs.push(TabModel.roleTabs());
    return tabs.sort((t1, t2) => t1.index - t2.index);
  }

  private static usersTab(): TabModel {
    const tab: TabModel = new TabModel();
    tab._index = 0;
    tab._translateKey = TabModel.TRANSLATE_PREFIX + 'users';
    tab._lastElementPath = RouteUserManagement.USERS;
    tab._routeCommands = RouteUserManagement.USERS_COMMANDS;
    return tab;
  }

  private static roleTabs(): TabModel {
    const tab: TabModel = new TabModel();
    tab._index = 1;
    tab._translateKey = TabModel.TRANSLATE_PREFIX + 'roles';
    tab._lastElementPath = RouteUserManagement.ROLES;
    tab._routeCommands = RouteUserManagement.ROLES_COMMANDS;
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

}
