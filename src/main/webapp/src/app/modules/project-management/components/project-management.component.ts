import {Component, OnInit} from '@angular/core';
import {MatTabChangeEvent} from '@angular/material/tabs/tab-group';
import {Router} from '@angular/router';
import {Authority} from '../../../common/api/api-models';
import {RouteProjectManagement} from '../../../common/const/routes';
import {TabModel} from '../../../common/models/tab.model';
import {TabUtil} from '../../../common/utils/tab.util';
import {AuthHelper} from '../../../core/auth/auth.helper';

@Component({
  templateUrl: './project-management.component.html',
  styleUrls: ['./project-management.component.scss']
})
export class ProjectManagementComponent implements OnInit {

  tabs: ProjectManagementTabModel[] = [];
  activeTabIndex: number;

  readonly projectsTabIndex: number = ProjectManagementTabModel.PROJECTS_TAB_INDEX;
  readonly legalActsTabIndex: number = ProjectManagementTabModel.LEGAL_ACTS_TAB_INDEX;

  constructor(
    private router: Router,
    private authHelper: AuthHelper
  ) {
  }

  onTabChange(changeEvent: MatTabChangeEvent): void {
    TabUtil.onTabChange(this.tabs, this.router, changeEvent);
  }

  ngOnInit(): void {
    this.tabs = ProjectManagementTabModel.getTabs(this.authHelper);
    this.activeTabIndex = TabUtil.getActiveTabIndexFromRouter(this.tabs, this.router);
  }

}

class ProjectManagementTabModel extends TabModel {

  static readonly PROJECTS_TAB_INDEX: number = 0;
  static readonly LEGAL_ACTS_TAB_INDEX: number = 1;

  protected get translatePrefix(): string {
    return 'project-management.';
  }

  static getTabs(authHelper: AuthHelper): ProjectManagementTabModel[] {
    const tabs: ProjectManagementTabModel[] = [];
    tabs.push(ProjectManagementTabModel.projectsTab());
    tabs.push(ProjectManagementTabModel.legalActsTab());
    return TabUtil.filterAndSortTabs(tabs, authHelper);
  }

  private static projectsTab(): ProjectManagementTabModel {
    const tab: ProjectManagementTabModel = new ProjectManagementTabModel();
    tab._index = ProjectManagementTabModel.PROJECTS_TAB_INDEX;
    tab._translateKey = tab.translatePrefix + 'tab-projects';
    tab._lastElementPath = RouteProjectManagement.PROJECTS;
    tab._routeCommands = RouteProjectManagement.PROJECTS_COMMANDS;
    tab._requiredAuthorities = [Authority.PROJECT_READ, Authority.PROJECT_WRITE, Authority.LEGAL_ACTS_READ, Authority.LEGAL_ACTS_WRITE];
    return tab;
  }

  private static legalActsTab(): ProjectManagementTabModel {
    const tab: ProjectManagementTabModel = new ProjectManagementTabModel();
    tab._index = ProjectManagementTabModel.LEGAL_ACTS_TAB_INDEX;
    tab._translateKey = tab.translatePrefix + 'tab-legal-acts';
    tab._lastElementPath = RouteProjectManagement.LEGAL_ACTS;
    tab._routeCommands = RouteProjectManagement.LEGAL_ACTS_COMMANDS;
    tab._requiredAuthorities = [Authority.LEGAL_ACTS_READ, Authority.LEGAL_ACTS_WRITE];
    return tab;
  }

}
