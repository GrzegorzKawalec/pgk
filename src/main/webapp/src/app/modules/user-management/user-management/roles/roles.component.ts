import {AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSelectChange} from '@angular/material/select';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {Router} from '@angular/router';
import {Subject} from 'rxjs';
import {debounceTime, finalize, takeUntil} from 'rxjs/operators';
import {Authority, RoleCriteria, RoleDTO} from '../../../../common/api/api-models';
import {Page} from '../../../../common/api/api-pagination.models';
import {BaseComponent} from '../../../../common/components/base.component';
import {RouteUserManagement} from '../../../../common/const/routes';
import {AuthorityTranslateModel, AuthorityTranslateService} from '../../../../common/services/authority-translate.service';
import {CriteriaBuilder, DirectionMapper} from '../../../../common/utils/criteria.util';
import {RoleService} from '../../services/role.service';

@Component({
  selector: 'pgk-roles',
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.scss']
})
export class RolesComponent extends BaseComponent implements OnInit, AfterViewInit {

  readonly requiredUpsertAuthorities: Authority[] = [Authority.USER_WRITE];

  readonly prefixTranslateColumn: string = 'user-management.role.columns.';

  readonly clnName: string = 'name';
  readonly clnAuthorities: string = 'authorities';
  readonly clnButtons: string = 'buttons';
  readonly displayedColumns: string[] = [this.clnName, this.clnAuthorities, this.clnButtons];
  tableData: MatTableDataSource<RoleTableModel>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  loading: boolean = true;

  private criteria: RoleCriteria = CriteriaBuilder.init(this.clnName);

  authoritySelectModels: AuthoritySelectModel[] = [];
  selectedAuthorityModels: AuthoritySelectModel[] = [];
  private authoritiesFilters: Authority[] = undefined;

  filterText: string;
  private applyTextFilter$: Subject<string> = new Subject();

  constructor(
    private router: Router,
    private cdr: ChangeDetectorRef,
    private roleService: RoleService,
    private authorityTranslateService: AuthorityTranslateService
  ) {
    super();
  }

  ngOnInit(): void {
    this.prepareAuthoritySelectModels();
    this.searchRole();
    this.subscribeTextFilter();
  }

  ngAfterViewInit(): void {
    this.paginator.pageSize = this.criteria.searchPage.pageSize;
    this.cdr.detectChanges();
    this.subscribeSort();
    this.subscribePaginator();
  }

  clickAddRole(): void {
    this.router.navigate(RouteUserManagement.ROLES_UPSERT_COMMANDS);
  }

  clearFilter(): void {
    if (!this.criteria.searchBy && !this.criteria.authorities) {
      return;
    }

    this.filterText = null;
    this.authoritiesFilters = undefined;
    this.selectedAuthorityModels = [];

    this.criteria.searchBy = null;
    this.criteria.authorities = null;

    this.searchRole();
  }

  changeAuthoritiesFilter(matSelect: MatSelectChange): void {
    this.authoritiesFilters = matSelect.value;
  }

  applyAuthoritiesFilter(): void {
    if (this.criteria.authorities === this.authoritiesFilters) {
      return;
    }
    this.criteria.authorities = this.authoritiesFilters;
    this.searchRole();
  }

  applyTextFilter(event: KeyboardEvent): void {
    const filterValue: string = (event.target as HTMLInputElement).value;
    this.applyTextFilter$.next(filterValue);
  }

  private subscribeTextFilter(): void {
    this.applyTextFilter$
      .pipe(
        takeUntil(this.destroy$),
        debounceTime(300)
      ).subscribe((filterText: string) => {
      this.criteria.searchBy = filterText;
      this.searchRole();
    });
  }

  private subscribeSort(): void {
    this.sort.sortChange
      .pipe(takeUntil(this.destroy$))
      .subscribe((sort) => {
        this.criteria.searchPage.sorting[0].direction = DirectionMapper.map(sort.direction);
        this.searchRole();
      });
  }

  private subscribePaginator(): void {
    this.paginator.page
      .pipe(takeUntil(this.destroy$))
      .subscribe((pageEvent) => {
        const pageSize: number = pageEvent.pageSize;
        const pageIndex: number = pageEvent.pageIndex;
        if (pageSize !== this.criteria.searchPage.pageSize || pageIndex !== this.criteria.searchPage.pageNumber) {
          this.criteria.searchPage.pageSize = pageSize;
          this.criteria.searchPage.pageNumber = pageIndex;
          this.searchRole();
        }
      });
  }

  private searchRole(): void {
    this.loading = true;
    this.roleService.find(this.criteria)
      .pipe(finalize(() => this.loading = false))
      .subscribe((page: Page<RoleDTO>) => {
        if (page) {
          this.prepareTableModel(page.content);
          this.paginator.length = page.totalElements;
        }
      });
  }

  private prepareTableModel(roles: RoleDTO[]): void {
    const tableModels: RoleTableModel[] = !roles ? [] :
      roles.map(role => this.prepareSingleRoleModel(role));
    this.tableData = new MatTableDataSource(tableModels);
  }

  private prepareSingleRoleModel(role: RoleDTO): RoleTableModel {
    return {
      ...role,
      authorityTranslateModels: this.prepareAuthorityTranslateModels(role.authorities)
    };
  }

  private prepareAuthorityTranslateModels(authorities: Authority[]): AuthorityTranslateModel[] {
    const authorityTranslateModels: AuthorityTranslateModel[] = !authorities ? [] :
      authorities.map(authority => this.authorityTranslateService.translate(authority));
    return authorityTranslateModels
      .sort((a, b) =>
        (a.name > b.name) ? 1 : ((b.name > a.name) ? -1 : 0)
      );
  }

  private prepareAuthoritySelectModels(): void {
    this.authoritySelectModels = Object.values(Authority).map((authority) => {
      return {
        authority,
        translate: this.authorityTranslateService.translateName(authority)
      } as AuthoritySelectModel;
    }).sort((a, b) =>
      (a.translate > b.translate) ? 1 : ((b.translate > a.translate) ? -1 : 0)
    );
  }

}

interface RoleTableModel extends RoleDTO {
  authorityTranslateModels: AuthorityTranslateModel[];
}

interface AuthoritySelectModel {
  authority: Authority;
  translate: string;
}
