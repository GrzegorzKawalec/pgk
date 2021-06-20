import {AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatSelectChange} from '@angular/material/select';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {Subject} from 'rxjs';
import {debounceTime, finalize, takeUntil} from 'rxjs/operators';
import {Authority, RoleDTO, UserCriteria, UserDTO, UserSearchDTO} from '../../../../common/api/api-models';
import {Page} from '../../../../common/api/api-pagination.models';
import {BaseComponent} from '../../../../common/components/base.component';
import {ModalConfirmComponent} from '../../../../common/components/modal-confirm/modal-confirm.component';
import {ModalConfirmModel} from '../../../../common/components/modal-confirm/modal-confirm.model';
import {RouteUserManagement} from '../../../../common/const/routes';
import {LocalStorageKey} from '../../../../common/services/local-storage/local-storage-key';
import {PaginatorService} from '../../../../common/services/paginator.service';
import {AuthorityUtil} from '../../../../common/utils/authority.util';
import {CriteriaBuilder, DirectionMapper} from '../../../../common/utils/criteria.util';
import {AuthHelper} from '../../../../core/auth/auth.helper';
import {RoleService} from '../../services/role.service';
import {UserManagementService} from '../../services/user-management.service';
import {UserDetailsModalComponent} from './user-details-modal/user-details-modal.component';

@Component({
  selector: 'pgk-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent extends BaseComponent implements OnInit, AfterViewInit {

  readonly requiredUpsertAuthorities: Authority[] = [Authority.ROLE_WRITE, Authority.USER_WRITE];
  readonly requiredReadUserDetailsAuthorities: Authority[] = [...this.requiredUpsertAuthorities, Authority.USER_READ];

  readonly prefixTranslateColumn: string = 'user-management.user.columns.';

  readonly pageSizeOptions: number[] = this.paginatorService.pageSizeOptions;

  readonly clnEmail: string = 'email';
  readonly clnFirstName: string = 'first-name';
  readonly clnLastName: string = 'last-name';
  readonly clnPhoneNumber: string = 'phone-number';
  readonly clnRole: string = 'role';
  readonly clnButtons: string = 'buttons';
  readonly displayedColumns: string[] = [
    this.clnEmail, this.clnFirstName, this.clnLastName, this.clnPhoneNumber, this.clnRole, this.clnButtons
  ];
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
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef,
    private authHelper: AuthHelper,
    private roleService: RoleService,
    private translateService: TranslateService,
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

  hasUnmodifiableAuthority(user: UserSearchDTO): boolean {
    if (!user || !user.role) {
      return false;
    }
    return AuthorityUtil.hasUnmodifiableAuthority(user.role.authorities, true);
  }

  clickAddUser(): void {
    this.router.navigate(RouteUserManagement.USERS_UPSERT_COMMANDS);
  }

  clickDetails(user: UserSearchDTO): void {
    this.dialog.open(UserDetailsModalComponent, {data: user.user, minWidth: '500px'});
  }

  clickEdit(user: UserSearchDTO): void {
    if (this.authHelper.hasAuthorities(this.requiredUpsertAuthorities)) {
      this.router.navigate([...RouteUserManagement.USERS_UPSERT_COMMANDS, user.user.id]);
    }
  }

  clickDeactivate(user: UserSearchDTO): void {
    if (this.authHelper.hasAuthorities(this.requiredUpsertAuthorities)) {
      const confirmModel: ModalConfirmModel = this.prepareModelForConfirmDeleteModal(user.user);
      const dialogRef: MatDialogRef<ModalConfirmComponent> = this.dialog.open(ModalConfirmComponent, {data: confirmModel});
      dialogRef.afterClosed()
        .pipe(takeUntil(this.destroy$))
        .subscribe(confirmed => confirmed === true && this.deactivateUser(user.user.id));
    }
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
          this.paginator.length = page.totalElements;
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

  private prepareModelForConfirmDeleteModal(user: UserDTO): ModalConfirmModel {
    const content: string = this.translateService.instant('user-management.user.trying-to-deactivate-user') + ': ' +
      user.firstName + ' ' + user.lastName + ' (' + user.email + ')';
    return {
      titleTranslateKey: 'user-management.user.want-to-deactivate-user',
      showDefaultContent: false,
      content: content
    };
  }

  private deactivateUser(userId: number): void {
    this.loading = true;
    this.userService.deactivate(userId)
      .pipe(finalize(() => this.loading = false))
      .subscribe(() => {
        const message: string = this.translateService.instant('common.deactivated');
        this.snackBar.open(message, 'OK', {duration: 2000});
        this.searchUser();
      });
  }

}
