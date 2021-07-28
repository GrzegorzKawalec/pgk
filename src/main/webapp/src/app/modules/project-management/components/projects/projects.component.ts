import {AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {MatSlideToggleChange} from '@angular/material/slide-toggle';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {finalize, takeUntil} from 'rxjs/operators';
import {Authority, ProjectCriteria, ProjectDTO, ProjectOrderByType, SelectDTO} from '../../../../common/api/api-models';
import {Page} from '../../../../common/api/api-pagination.models';
import {BaseTableComponent} from '../../../../common/components/base/base-table.component';
import {ModalConfirmComponent} from '../../../../common/components/modal-confirm/modal-confirm.component';
import {ModalConfirmModel} from '../../../../common/components/modal-confirm/modal-confirm.model';
import {RouteProjectManagement} from '../../../../common/const/routes';
import {LocalStorageKey} from '../../../../common/services/local-storage/local-storage-key';
import {PaginatorService} from '../../../../common/services/paginator.service';
import {CriteriaBuilder, DirectionMapper} from '../../../../common/utils/criteria.util';
import {StringUtils} from '../../../../common/utils/string.utils';
import {AuthHelper} from '../../../../core/auth/auth.helper';
import {ProjectService} from '../../services/project.service';
import {ProjectDetailsModalComponent} from './project-details-modal/project-details-modal.component';

@Component({
  selector: 'pgk-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.scss']
})
export class ProjectsComponent extends BaseTableComponent<ProjectCriteria> implements OnInit, AfterViewInit {

  readonly requiredUpsertAuthorities: Authority[] = [Authority.PROJECT_WRITE, Authority.LEGAL_ACTS_WRITE];

  readonly prefixTranslateMessage: string = 'project-management.projects.';
  readonly prefixTranslateColumn: string = this.prefixTranslateMessage + 'columns.';

  readonly clnName: string = 'name';
  readonly clnProjectManager: string = 'project-manager';
  readonly clnDateStart: string = 'date-start';
  readonly clnDateEnd: string = 'date-end';
  readonly clnButtons: string = 'buttons';
  readonly displayedColumns: string[] = [this.clnName, this.clnProjectManager, this.clnDateStart, this.clnDateEnd, this.clnButtons];
  tableData: MatTableDataSource<ProjectDTO>;
  @ViewChild(MatSort) sort: MatSort;

  criteria: ProjectCriteria = CriteriaBuilder.init(this.clnName, {isActive: true});

  filterLegalAct: SelectDTO;
  availableLegalAct: SelectDTO[] = [];
  private loadingLegalAct: boolean = false;

  filterParticipant: SelectDTO;
  availableParticipant: SelectDTO[] = [];
  private loadingParticipants: boolean = false;

  emptySelect: SelectDTO = {id: 0, value: '', additionalInfo: ''};

  constructor(
    private router: Router,
    private dialog: MatDialog,
    private authHelper: AuthHelper,
    private projectService: ProjectService,
    translateService: TranslateService,
    paginatorService: PaginatorService,
    snackBar: MatSnackBar,
    cdr: ChangeDetectorRef
  ) {
    super(translateService, snackBar, cdr, paginatorService, LocalStorageKey.PROJECTS_PER_PAGE);
  }

  ngOnInit(): void {
    this.subscribeTextFilter();
    this.loadSelectData();
  }

  ngAfterViewInit(): void {
    this.initPageSize();
    this.subscribeSort();
    this.subscribePaginator();
  }

  clickAddProject(): void {
    this.router.navigate(RouteProjectManagement.PROJECTS_UPSERT_COMMANDS);
  }

  clickDetails(project: ProjectDTO): void {
    this.dialog.open(ProjectDetailsModalComponent, {data: project, minWidth: '750px'});
  }

  clickEdit(project: ProjectDTO): void {
    if (this.authHelper.hasAuthorities(this.requiredUpsertAuthorities)) {
      this.router.navigate([...RouteProjectManagement.PROJECTS_UPSERT_COMMANDS, project.id]);
    }
  }

  clickDeactivate(project: ProjectDTO): void {
    this.changeProjectStatus(project, true);
  }

  clickActivate(project: ProjectDTO): void {
    this.changeProjectStatus(project, false);
  }

  clearFilter(): void {
    if (!this.criteria.searchBy && !this.criteria.legalActId && !this.criteria.participantId) {
      return;
    }

    this.filterText = null;
    this.filterLegalAct = null;
    this.filterParticipant = null;

    this.criteria.searchBy = null;
    this.criteria.legalActId = null;
    this.criteria.participantId = null;

    this.search();
  }

  applyLegalActFilter(): void {
    this.criteria.legalActId = this.filterLegalAct && this.filterLegalAct.id ? this.filterLegalAct.id : null;
    this.search();
  }

  applyParticipantFilter(): void {
    this.criteria.participantId = this.filterParticipant && this.filterParticipant.id ? this.filterParticipant.id : null;
    this.search();
  }

  applyInactiveFilter(inactive: MatSlideToggleChange): void {
    this.criteria.isActive = !inactive.checked;
    this.search();
  }

  private subscribeSort(): void {
    this.criteria.searchPage.sorting = null;
    this.sort.sortChange
      .pipe(takeUntil(this.destroy$))
      .subscribe((sort: Sort) => {
        const orderBy: ProjectOrderByType = this.mapSortProperty(sort);
        if (!orderBy) {
          this.criteria.orderBy = null;
          this.criteria.orderDirection = null;
        } else {
          this.criteria.orderBy = orderBy;
          this.criteria.orderDirection = DirectionMapper.map(sort.direction);
        }
        this.search();
      });
  }

  private mapSortProperty(sort: Sort): ProjectOrderByType {
    if (!sort || !sort.active) {
      return null;
    }
    if (sort.active === this.clnName) {
      return ProjectOrderByType.name;
    }
    if (sort.active === this.clnDateStart) {
      return ProjectOrderByType.dateStart;
    }
    if (sort.active === this.clnDateEnd) {
      return ProjectOrderByType.dateEnd;
    }
    if (sort.active === this.clnProjectManager) {
      return ProjectOrderByType.projectManager;
    }
    return null;
  }

  private loadSelectData(): void {
    this.loading = true;
    this.loadSelectLegalActs();
    this.loadSelectParticipants();
  }

  private loadSelectLegalActs(): void {
    this.loadingLegalAct = true;
    this.projectService.getSelectLegalActs()
      .pipe(finalize(() => this.loadingLegalAct = false))
      .subscribe((selectLegalActs: SelectDTO[]) => {
        this.availableLegalAct = selectLegalActs || [];
        this.availableLegalAct.sort((a, b) => StringUtils.compareString(a.value, b.value));
        if (!this.loadingParticipants) {
          this.search();
        }
      });
  }

  private loadSelectParticipants(): void {
    this.loadingParticipants = true;
    this.projectService.getSelectParticipants()
      .pipe(finalize(() => this.loadingParticipants = false))
      .subscribe((selectParticipants: SelectDTO[]) => {
        this.availableParticipant = selectParticipants || [];
        this.availableParticipant.sort((a, b) => StringUtils.compareString(a.value, b.value));
        if (!this.loadingLegalAct) {
          this.search();
        }
      });
  }

  protected search(): void {
    this.loading = true;
    this.projectService.find(this.criteria)
      .pipe(finalize(() => this.loading = false))
      .subscribe((page: Page<ProjectDTO>) => {
        if (page) {
          this.tableData = new MatTableDataSource(page.content);
          this.paginator.length = page.totalElements;
        }
      });
  }

  private changeProjectStatus(project: ProjectDTO, forDeactivate: boolean): void {
    if (!this.authHelper.hasAuthorities(this.requiredUpsertAuthorities)) {
      return;
    }
    const confirmModel: ModalConfirmModel = forDeactivate ?
      this.prepareModelForConfirmDeactivateModal(project) :
      this.prepareModelForConfirmActivateModal(project);
    const dialogRef: MatDialogRef<ModalConfirmComponent> = this.dialog.open(ModalConfirmComponent, {data: confirmModel});
    dialogRef.afterClosed()
      .pipe(takeUntil(this.destroy$))
      .subscribe(confirmed => {
        if (confirmed === true) {
          forDeactivate ? this.deactivateProject(project.id) : this.activateProject(project.id);
        }
      });
  }

  private prepareModelForConfirmDeactivateModal(project: ProjectDTO): ModalConfirmModel {
    return this.prepareModelForConfirmForChangeProjectStatus(project, 'want-to-deactivate-project');
  }

  private prepareModelForConfirmActivateModal(project: ProjectDTO): ModalConfirmModel {
    return this.prepareModelForConfirmForChangeProjectStatus(project, 'want-to-activate-project');
  }

  private prepareModelForConfirmForChangeProjectStatus(project: ProjectDTO, suffixTitleKey: string): ModalConfirmModel {
    const content: string = project.name;
    return {
      titleTranslateKey: this.prefixTranslateMessage + suffixTitleKey,
      showDefaultContent: false,
      content: content
    };
  }

  private deactivateProject(projectId: number): void {
    this.loading = true;
    this.projectService.deactivate(projectId)
      .subscribe(() => this.showSnackBarAfterChangeStatus('common.deactivated'));
  }

  private activateProject(projectId: number): void {
    this.loading = true;
    this.projectService.activate(projectId)
      .subscribe(() => this.showSnackBarAfterChangeStatus('common.activated'));
  }

}
