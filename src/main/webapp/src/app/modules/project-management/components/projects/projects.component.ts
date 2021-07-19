import {AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatSlideToggleChange} from '@angular/material/slide-toggle';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {Router} from '@angular/router';
import {Subject} from 'rxjs';
import {debounceTime, finalize, takeUntil} from 'rxjs/operators';
import {Authority, ProjectCriteria, ProjectDTO, ProjectOrderByType, SelectDTO} from '../../../../common/api/api-models';
import {Page} from '../../../../common/api/api-pagination.models';
import {BaseComponent} from '../../../../common/components/base.component';
import {RouteProjectManagement} from '../../../../common/const/routes';
import {LocalStorageKey} from '../../../../common/services/local-storage/local-storage-key';
import {PaginatorService} from '../../../../common/services/paginator.service';
import {CriteriaBuilder, DirectionMapper} from '../../../../common/utils/criteria.util';
import {StringUtils} from '../../../../common/utils/string.utils';
import {ProjectService} from '../../services/project.service';

@Component({
  selector: 'pgk-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.scss']
})
export class ProjectsComponent extends BaseComponent implements OnInit, AfterViewInit {

  readonly requiredUpsertAuthorities: Authority[] = [Authority.PROJECT_WRITE, Authority.LEGAL_ACTS_WRITE];

  readonly prefixTranslateMessage: string = 'project-management.projects.';
  readonly prefixTranslateColumn: string = this.prefixTranslateMessage + 'columns.';

  readonly pageSizeOptions: number[] = this.paginatorService.pageSizeOptions;

  readonly clnName: string = 'name';
  readonly clnDateStart: string = 'date-start';
  readonly clnDateEnd: string = 'date-end';
  readonly clnProjectManager: string = 'project-manager';
  readonly displayedColumns: string[] = [this.clnName, this.clnDateStart, this.clnDateEnd, this.clnProjectManager];
  tableData: MatTableDataSource<ProjectDTO>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  loading: boolean = false;

  criteria: ProjectCriteria = CriteriaBuilder.init(this.clnName, {isActive: true});

  filterText: string;
  private applyTextFilter$: Subject<string> = new Subject();

  filterLegalAct: SelectDTO;
  availableLegalAct: SelectDTO[] = [];
  private loadingLegalAct: boolean = false;

  filterParticipant: SelectDTO;
  availableParticipant: SelectDTO[] = [];
  private loadingParticipants: boolean = false;

  emptySelect: SelectDTO = {id: 0, value: '', additionalInfo: ''};

  constructor(
    private router: Router,
    private cdr: ChangeDetectorRef,
    private projectService: ProjectService,
    private paginatorService: PaginatorService
  ) {
    super();
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

    this.searchProjects();
  }

  applyTextFilter(event: KeyboardEvent): void {
    const filterValue: string = (event.target as HTMLInputElement).value;
    this.applyTextFilter$.next(filterValue);
  }

  applyLegalActFilter(): void {
    this.criteria.legalActId = this.filterLegalAct && this.filterLegalAct.id ? this.filterLegalAct.id : null;
    this.searchProjects();
  }

  applyParticipantFilter(): void {
    this.criteria.participantId = this.filterParticipant && this.filterParticipant.id ? this.filterParticipant.id : null;
    this.searchProjects();
  }

  applyInactiveFilter(inactive: MatSlideToggleChange): void {
    this.criteria.isActive = !inactive.checked;
    this.searchProjects();
  }

  private subscribeTextFilter(): void {
    this.applyTextFilter$
      .pipe(
        takeUntil(this.destroy$),
        debounceTime(300)
      ).subscribe((filterText: string) => {
      this.criteria.searchBy = filterText;
      this.searchProjects();
    });
  }

  private initPageSize(): void {
    const pageSize: number = this.paginatorService.lastPageSize(LocalStorageKey.PROJECTS_PER_PAGE);
    this.criteria.searchPage.pageSize = pageSize;
    this.paginator.pageSize = pageSize;
    this.cdr.detectChanges();
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
        this.searchProjects();
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

  private subscribePaginator(): void {
    this.paginator.page
      .pipe(takeUntil(this.destroy$))
      .subscribe((pageEvent: PageEvent) => this.afterChangePaginatorFields(pageEvent));
  }

  private afterChangePaginatorFields(pageEvent: PageEvent): void {
    const pageSize: number = pageEvent.pageSize;
    const pageIndex: number = pageEvent.pageIndex;
    if (pageSize !== this.criteria.searchPage.pageSize || pageIndex !== this.criteria.searchPage.pageNumber) {
      this.paginatorService.changeLastPageSize(LocalStorageKey.LEGAL_ACTS_PER_PAGE, pageSize);
      this.criteria.searchPage.pageSize = pageSize;
      this.criteria.searchPage.pageNumber = pageIndex;
      this.searchProjects();
    }
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
          this.searchProjects();
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
          this.searchProjects();
        }
      });
  }

  private searchProjects(): void {
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

}
