import {AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {MatSelectChange} from '@angular/material/select';
import {MatSlideToggleChange} from '@angular/material/slide-toggle';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {finalize, takeUntil} from 'rxjs/operators';
import {Authority, Direction, RoleDTO, UserCriteria, UserDTO, UserSearchDTO} from '../../../../common/api/api-models';
import {Page} from '../../../../common/api/api-pagination.models';
import {BaseTableComponent} from '../../../../common/components/base/base-table.component';
import {ModalConfirmComponent} from '../../../../common/components/modal-confirm/modal-confirm.component';
import {ModalConfirmModel} from '../../../../common/components/modal-confirm/modal-confirm.model';
import {PREFIX_NUMBER_PHONE} from '../../../../common/const/const';
import {RouteUserManagement} from '../../../../common/const/routes';
import {LocalStorageKey} from '../../../../common/services/local-storage/local-storage-key';
import {PaginatorService} from '../../../../common/services/paginator.service';
import {UserService} from '../../../../common/services/user.service';
import {AuthorityUtil} from '../../../../common/utils/authority.util';
import {CriteriaBuilder, DirectionMapper} from '../../../../common/utils/criteria.util';
import {AuthHelper} from '../../../../core/auth/auth.helper';
import {RoleService} from '../../services/role.service';
import {UserManagementService} from '../../services/user-management.service';
import {UserChangePasswordModalComponent} from './user-change-password-modal/user-change-password-modal.component';
import {UserDetailsModalComponent} from './user-details-modal/user-details-modal.component';

@Component({
  selector: 'pgk-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent extends BaseTableComponent<UserCriteria> implements OnInit, AfterViewInit {

  readonly requiredUpsertAuthorities: Authority[] = [Authority.ROLE_WRITE, Authority.USER_WRITE];
  readonly requiredReadUserDetailsAuthorities: Authority[] = [...this.requiredUpsertAuthorities, Authority.USER_READ];

  readonly prefixTranslateMessage: string = 'user-management.user.';
  readonly prefixTranslateColumn: string = this.prefixTranslateMessage + 'columns.';

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
  @ViewChild(MatSort) sort: MatSort;

  criteria: UserCriteria = CriteriaBuilder.init(this.clnEmail, {isActive: true});

  roles: RoleDTO[] = [];
  selectedRoles: RoleDTO[] = [];
  private roleIdsFilters: number[] = undefined;

  constructor(
    private router: Router,
    private dialog: MatDialog,
    private authHelper: AuthHelper,
    private roleService: RoleService,
    private userService: UserService,
    private userManagementService: UserManagementService,
    translateService: TranslateService,
    paginatorService: PaginatorService,
    snackBar: MatSnackBar,
    cdr: ChangeDetectorRef
  ) {
    super(translateService, snackBar, cdr, paginatorService, LocalStorageKey.USERS_ITEM_PER_PAGE);
  }

  ngOnInit(): void {
    this.loadRoles();
    this.search();
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

  canChangePassword(user: UserSearchDTO): boolean {
    const loggedUser: UserDTO = this.userService.loggedUser;
    if (user.user.id === loggedUser.id) {
      return true;
    }
    return this.authHelper.hasAuthorities([Authority.ADMIN]);
  }

  preparePhoneNumberForDisplay(user: UserDTO): string {
    if (!user || !user.phoneNumber) {
      return '';
    }
    if (user.phoneNumber.startsWith(PREFIX_NUMBER_PHONE)) {
      const numberWithoutPrefix: string = user.phoneNumber.slice(PREFIX_NUMBER_PHONE.length);
      if (!numberWithoutPrefix.startsWith(' ')) {
        return PREFIX_NUMBER_PHONE + ' ' + numberWithoutPrefix;
      }
    }
    return user.phoneNumber;
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
    this.changeUserStatus(user.user, true);
  }

  clickActivate(user: UserSearchDTO): void {
    this.changeUserStatus(user.user, false);
  }

  clickChangePassword(user: UserSearchDTO): void {
    if (!this.canChangePassword(user)) {
      return;
    }
    const dialogRef: MatDialogRef<UserChangePasswordModalComponent> = this.dialog.open(
      UserChangePasswordModalComponent, {data: user.user, minWidth: '250px'}
    );
    dialogRef.afterClosed()
      .pipe(takeUntil(this.destroy$))
      .subscribe(updated => {
        if (updated === true) {
          this.showSnackBar('common.updated');
        }
      });
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

    this.search();
  }

  applyInactiveFilter(inactive: MatSlideToggleChange): void {
    this.criteria.isActive = !inactive.checked;
    this.search();
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
    this.search();
  }

  private loadRoles(): void {
    this.roleService.all().subscribe((roles: RoleDTO[]) => this.roles = roles || []);
  }

  protected search(): void {
    this.loading = true;
    this.userManagementService.find(this.criteria)
      .pipe(finalize(() => this.loading = false))
      .subscribe((page: Page<UserSearchDTO>) => {
        if (page) {
          this.tableData = new MatTableDataSource(page.content);
          this.paginator.length = page.totalElements;
        }
      });
  }

  private subscribeSort(): void {
    this.sort.sortChange
      .pipe(takeUntil(this.destroy$))
      .subscribe((sort) => {
        const sortProperty: string = this.mapSortProperty(sort);
        if (!sortProperty) {
          this.criteria.searchPage.sorting = null;
          if (sort.active === this.clnRole) {
            this.criteria.orderByRole = true;
            const orderBy: Direction = DirectionMapper.map(sort.direction);
            this.criteria.orderByRoleAsc = Direction.ASC === orderBy;
          }
        } else {
          this.criteria.searchPage.sorting[0].direction = DirectionMapper.map(sort.direction);
          this.criteria.searchPage.sorting[0].property = sortProperty;
          this.criteria.orderByRole = false;
        }
        this.search();
      });
  }

  private mapSortProperty(sort: Sort): string {
    if (!sort || !sort.active) {
      return null;
    }
    if (sort.active === this.clnFirstName) {
      return 'firstName';
    }
    if (sort.active === this.clnLastName) {
      return 'lastName';
    }
    if (sort.active === this.clnPhoneNumber) {
      return 'phoneNumber';
    }
    if (sort.active === this.clnRole) {
      return null;
    }
    return sort.active;
  }

  private changeUserStatus(user: UserDTO, forDeactivate: boolean): void {
    if (!this.authHelper.hasAuthorities(this.requiredUpsertAuthorities)) {
      return;
    }
    const confirmModel: ModalConfirmModel = forDeactivate ?
      this.prepareModelForConfirmDeactivateModal(user) :
      this.prepareModelForConfirmActivateModal(user);
    const dialogRef: MatDialogRef<ModalConfirmComponent> = this.dialog.open(ModalConfirmComponent, {data: confirmModel});
    dialogRef.afterClosed()
      .pipe(takeUntil(this.destroy$))
      .subscribe(confirmed => {
        if (confirmed === true) {
          forDeactivate ? this.deactivateUser(user.id) : this.activateUser(user.id);
        }
      });
  }

  private prepareModelForConfirmDeactivateModal(user: UserDTO): ModalConfirmModel {
    return this.prepareModelForConfirmUserModal(user, 'trying-to-deactivate-user', 'want-to-deactivate-user');
  }

  private prepareModelForConfirmActivateModal(user: UserDTO): ModalConfirmModel {
    return this.prepareModelForConfirmUserModal(user, 'trying-to-activate-user', 'want-to-activate-user');
  }

  private prepareModelForConfirmUserModal(user: UserDTO, suffixContentKey: string, suffixTitleKey: string): ModalConfirmModel {
    const content: string = this.translateService.instant(this.prefixTranslateMessage + suffixContentKey) + ': ' +
      user.firstName + ' ' + user.lastName + ' (' + user.email + ')';
    return {
      titleTranslateKey: this.prefixTranslateMessage + suffixTitleKey,
      showDefaultContent: false,
      content: content
    };
  }

  private deactivateUser(userId: number): void {
    this.loading = true;
    this.userManagementService.deactivate(userId)
      .subscribe(() => this.showSnackBarAfterChangeStatus('common.deactivated'));
  }

  private activateUser(userId: number): void {
    this.loading = true;
    this.userManagementService.activate(userId)
      .subscribe(() => this.showSnackBarAfterChangeStatus('common.activated'))
  }

}
