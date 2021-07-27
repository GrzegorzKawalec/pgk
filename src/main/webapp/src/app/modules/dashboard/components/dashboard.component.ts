import {Component, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {finalize} from 'rxjs/operators';
import {DashboardDTO, LegalActDTO, ProjectDTO} from '../../../common/api/api-models';
import {LocalStorageKey} from '../../../common/services/local-storage/local-storage-key';
import {LocalStorageService} from '../../../common/services/local-storage/local-storage.service';
import {StringUtils} from '../../../common/utils/string.utils';
import {DashboardService} from '../services/dashboard.service';

@Component({
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  readonly prefixTranslateMessage: string = 'dashboard.';

  currentProject: ProjectDTO;
  visibleProjectIndex: number = -1;
  readonly projects: Map<number, ProjectDTO> = new Map<number, ProjectDTO>();

  loading: boolean = false;

  constructor(
    private dashboardService: DashboardService,
    private translateService: TranslateService,
    private localStorageService: LocalStorageService
  ) {
  }

  ngOnInit(): void {
    this.loadDashboard();
  }

  getPaginInfo(): string {
    if (!this.currentProject) {
      return '';
    }
    return (this.visibleProjectIndex + 1) +
      ' ' +
      this.translateService.instant(this.prefixTranslateMessage + 'page-of') +
      ' ' +
      this.projects.size;
  }

  needDisablePreviousPage(): boolean {
    return !this.currentProject || this.visibleProjectIndex < 1;
  }

  needDisableNextPage(): boolean {
    return !this.currentProject || this.projects.size <= this.visibleProjectIndex + 1;
  }

  clickNextPage(): void {
    if (!this.currentProject) {
      this.visibleProjectIndex = -1;
      this.currentProject = null;
      return;
    }
    if (this.visibleProjectIndex + 1 === this.projects.size) {
      this.visibleProjectIndex = this.projects.size - 1;
    } else {
      this.visibleProjectIndex += 1;
    }
    this.currentProject = this.projects.get(this.visibleProjectIndex);
    this.setLastVisibleProjectInLocalStorage();
  }

  clickPreviousPage(): void {
    if (!this.currentProject) {
      this.visibleProjectIndex = -1;
      this.currentProject = null;
      return;
    }
    if (this.visibleProjectIndex === 0) {
    } else {
      this.visibleProjectIndex -= 1;
    }
    this.currentProject = this.projects.get(this.visibleProjectIndex);
    this.setLastVisibleProjectInLocalStorage();
  }

  clickLegalActLink(legalAct: LegalActDTO): void {
    window.open(legalAct.link, '_blank');
  }

  private loadDashboard(): void {
    this.loading = true;
    this.dashboardService.getDashboard()
      .pipe(finalize(() => this.loading = false))
      .subscribe((dashboard: DashboardDTO) => this.prepareProjects(dashboard));
  }

  private prepareProjects(dashboard: DashboardDTO): void {
    this.projects.clear();
    this.visibleProjectIndex = -1;
    this.currentProject = null;
    if (!dashboard || !dashboard.projects || dashboard.projects.length < 1) {
      return;
    }
    const lastVisibleProjectId: number = this.localStorageService.read(LocalStorageKey.DASHBOARD_LAST_VISIBLE_PROJECT_ID);
    const projects: ProjectDTO[] = dashboard.projects.sort((a, b) => StringUtils.compareString(a.name, b.name));
    for (let i: number = 0; i < projects.length; i++) {
      this.projects.set(i, projects[i]);
      if (lastVisibleProjectId !== null && lastVisibleProjectId === projects[i].id) {
        this.visibleProjectIndex = i;
      }
    }
    if (this.visibleProjectIndex < 0) {
      this.visibleProjectIndex = 0;
    }
    this.currentProject = this.projects.get(this.visibleProjectIndex);
    this.setLastVisibleProjectInLocalStorage();
  }

  private setLastVisibleProjectInLocalStorage(): void {
    if (!this.currentProject) {
      return;
    }
    const lastVisibleProjectId: number = this.currentProject.id;
    this.localStorageService.write(LocalStorageKey.DASHBOARD_LAST_VISIBLE_PROJECT_ID, lastVisibleProjectId);
  }

}
