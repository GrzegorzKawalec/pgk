import {AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatSelectChange} from '@angular/material/select';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {Router} from '@angular/router';
import {Subject} from 'rxjs';
import {debounceTime, finalize, takeUntil} from 'rxjs/operators';
import {Authority, RoleDTO, UserCriteria, UserSearchDTO} from '../../../../common/api/api-models';
import {Page} from '../../../../common/api/api-pagination.models';
import {BaseComponent} from '../../../../common/components/base.component';
import {RouteUserManagement} from '../../../../common/const/routes';
import {LocalStorageKey} from '../../../../common/services/local-storage/local-storage-key';
import {PaginatorService} from '../../../../common/services/paginator.service';
import {CriteriaBuilder, DirectionMapper} from '../../../../common/utils/criteria.util';
import {RoleService} from '../../services/role.service';
import {UserManagementService} from '../../services/user-management.service';

@Component({
  selector: 'pgk-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent extends BaseComponent implements OnInit, AfterViewInit {

  readonly requiredUpsertAuthorities: Authority[] = [Authority.ROLE_WRITE];

  readonly prefixTranslateColumn: string = 'user-management.user.columns.';

  readonly pageSizeOptions: number[] = this.paginatorService.pageSizeOptions;

  readonly clnEmail: string = 'email';
  readonly clnFirstName: string = 'first-name';
  readonly clnLastName: string = 'last-name';
  readonly clnPhoneNumber: string = 'phone-number';
  readonly clnRole: string = 'role';
  readonly displayedColumns: string[] = [this.clnEmail, this.clnFirstName, this.clnLastName, this.clnPhoneNumber, this.clnRole];
  tableData: MatTableDataSource<UserSearchDTO>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  loading: boolean = false;

  private criteria: UserCriteria = CriteriaBuilder.init(this.clnEmail);

  roles: RoleDTO[] = [];
  selectedRoles: RoleDTO[] = [];
  private roleIdsFilters: number[] = undefined;

  filterText: string;
  private applyTextFilter$: Subject<string> = new Subject();

  constructor(
    private router: Router,
    private cdr: ChangeDetectorRef,
    private roleService: RoleService,
    private userService: UserManagementService,
    private paginatorService: PaginatorService
  ) {
    super();
  }

  ngOnInit(): void {
    this.loadRoles();
    this.searchUser();
    this.subscribeTextFilter();
  }

  ngAfterViewInit(): void {
    this.initPageSize();
    this.subscribeSort();
    this.subscribePaginator();
  }

  clickAddUser(): void {
    this.router.navigate(RouteUserManagement.USERS_UPSERT_COMMANDS);
  }

  clearFilter(): void {
    if (!this.criteria.searchBy && !this.criteria.roleIds) {
      return;
    }

    this.filterText = null;
    this.roleIdsFilters = undefined;
    this.selectedRoles = [];

    this.criteria.searchBy = null;
    this.criteria.roleIds = null;

    this.searchUser();
  }

  applyTextFilter(event: KeyboardEvent): void {
    const filterValue: string = (event.target as HTMLInputElement).value;
    this.applyTextFilter$.next(filterValue);
  }

  changeRolesFilter(matSelect: MatSelectChange): void {
    const selectedRoles: RoleDTO[] = matSelect.value || [];
    this.roleIdsFilters = selectedRoles.map(role => role.id);
  }

  applyRolesFilter(): void {
    if (this.criteria.roleIds === this.roleIdsFilters) {
      return;
    }
    this.criteria.roleIds = this.roleIdsFilters;
    this.searchUser();
  }

  private loadRoles(): void {
    this.roleService.all().subscribe((roles: RoleDTO[]) => this.roles = roles || []);
  }

  private searchUser(): void {
    this.loading = true;
    this.userService.find(this.criteria)
      .pipe(finalize(() => this.loading = false))
      .subscribe((page: Page<UserSearchDTO>) => {
        if (page) {
          this.tableData = new MatTableDataSource(page.content);
        }
      });
  }

  private subscribeTextFilter(): void {
    this.applyTextFilter$
      .pipe(
        takeUntil(this.destroy$),
        debounceTime(300)
      ).subscribe((filterText: string) => {
      this.criteria.searchBy = filterText;
      this.searchUser();
    });
  }

  private initPageSize(): void {
    const pageSize: number = this.paginatorService.lastPageSize(LocalStorageKey.USERS_ITEM_PER_PAGE);
    this.criteria.searchPage.pageSize = pageSize;
    this.paginator.pageSize = pageSize;
    this.cdr.detectChanges();
  }

  private subscribeSort(): void {
    this.sort.sortChange
      .pipe(takeUntil(this.destroy$))
      .subscribe((sort) => {
        this.criteria.searchPage.sorting[0].direction = DirectionMapper.map(sort.direction);
        this.searchUser();
      });
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
      this.paginatorService.changeLastPageSize(LocalStorageKey.USERS_ITEM_PER_PAGE, pageSize);
      this.criteria.searchPage.pageSize = pageSize;
      this.criteria.searchPage.pageNumber = pageIndex;
      this.searchUser();
    }
  }

}
