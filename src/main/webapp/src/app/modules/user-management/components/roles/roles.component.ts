import {AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatSelectChange} from '@angular/material/select';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {Subject} from 'rxjs';
import {debounceTime, finalize, takeUntil} from 'rxjs/operators';
import {Authority, RoleCriteria, RoleDTO} from '../../../../common/api/api-models';
import {Page} from '../../../../common/api/api-pagination.models';
import {BaseComponent} from '../../../../common/components/base.component';
import {ModalConfirmComponent} from '../../../../common/components/modal-confirm/modal-confirm.component';
import {ModalConfirmModel} from '../../../../common/components/modal-confirm/modal-confirm.model';
import {RouteUserManagement} from '../../../../common/const/routes';
import {AuthorityTranslateModel, AuthorityTranslateService} from '../../../../common/services/authority-translate.service';
import {LocalStorageKey} from '../../../../common/services/local-storage/local-storage-key';
import {PaginatorService} from '../../../../common/services/paginator.service';
import {AuthorityUtil} from '../../../../common/utils/authority.util';
import {CriteriaBuilder, DirectionMapper} from '../../../../common/utils/criteria.util';
import {AuthHelper} from '../../../../core/auth/auth.helper';
import {RoleService} from '../../services/role.service';
import {RoleDetailsModalComponent} from './role-details-modal/role-details-modal.component';

@Component({
  selector: 'pgk-roles',
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.scss']
})
export class RolesComponent extends BaseComponent implements OnInit, AfterViewInit {

  readonly requiredUpsertAuthorities: Authority[] = [Authority.ROLE_WRITE];

  readonly prefixTranslateMessage: string = 'user-management.role.';
  readonly prefixTranslateColumn: string = this.prefixTranslateMessage + 'columns.';

  readonly pageSizeOptions: number[] = this.paginatorService.pageSizeOptions;

  readonly clnName: string = 'name';
  readonly clnAuthorities: string = 'authorities';
  readonly clnButtons: string = 'buttons';
  readonly displayedColumns: string[] = [this.clnName, this.clnAuthorities, this.clnButtons];
  tableData: MatTableDataSource<RoleTableModel>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  loading: boolean = false;

  private criteria: RoleCriteria = CriteriaBuilder.init(this.clnName);

  authoritySelectModels: AuthoritySelectModel[] = [];
  selectedAuthorityModels: AuthoritySelectModel[] = [];
  private authoritiesFilters: Authority[] = undefined;

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
    private paginatorService: PaginatorService,
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
    this.initPageSize();
    this.subscribeSort();
    this.subscribePaginator();
  }

  hasUnmodifiableAuthority(role: RoleTableModel): boolean {
    if (!role) {
      return false;
    }
    return AuthorityUtil.hasUnmodifiableAuthority(role.authorities);
  }

  clickAddRole(): void {
    this.router.navigate(RouteUserManagement.ROLES_UPSERT_COMMANDS);
  }

  clickDetails(role: RoleTableModel): void {
    this.dialog.open(RoleDetailsModalComponent, {data: role, minWidth: '500px', width: '50%'});
  }

  clickEdit(role: RoleTableModel): void {
    if (this.authHelper.hasAuthorities(this.requiredUpsertAuthorities)) {
      this.router.navigate([...RouteUserManagement.ROLES_UPSERT_COMMANDS, role.id]);
    }
  }

  clickDelete(role: RoleTableModel): void {
    if (this.authHelper.hasAuthorities(this.requiredUpsertAuthorities)) {
      const confirmModel: ModalConfirmModel = this.prepareModelForConfirmDeleteModal(role);
      const dialogRef: MatDialogRef<ModalConfirmComponent> = this.dialog.open(ModalConfirmComponent, {data: confirmModel});
      dialogRef.afterClosed()
        .pipe(takeUntil(this.destroy$))
        .subscribe(confirmed => confirmed === true && this.deleteRole(role.id));
    }
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
      .subscribe((sort: Sort) => {
        const sortProperty: string = RolesComponent.mapSortProperty(sort);
        if (!sortProperty) {
          this.criteria.searchPage.sorting = null;
        } else {
          this.criteria.searchPage.sorting[0].direction = DirectionMapper.map(sort.direction);
          this.criteria.searchPage.sorting[0].property = sortProperty;
        }
        this.searchRole();
      });
  }

  private static mapSortProperty(sort: Sort): string {
    if (!sort || !sort.active) {
      return null;
    }
    return sort.active;
  }

  private subscribePaginator(): void {
    this.paginator.page
      .pipe(takeUntil(this.destroy$))
      .subscribe((pageEvent: PageEvent) => this.afterChangePaginatorFields(pageEvent));
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

  private prepareModelForConfirmDeleteModal(role: RoleTableModel): ModalConfirmModel {
    const content: string = this.translateService.instant(this.prefixTranslateMessage + 'trying-to-delete-role') + ': ' + role.name + '.';
    return {
      titleTranslateKey: this.prefixTranslateMessage + 'want-to-delete-role',
      showDefaultContent: true,
      content: content
    };
  }

  private deleteRole(roleId: number): void {
    this.loading = true;
    this.roleService.delete(roleId)
      .pipe(finalize(() => this.loading = false))
      .subscribe(() => {
        const message: string = this.translateService.instant('common.deleted');
        this.snackBar.open(message, 'OK', {duration: 2000});
        this.searchRole();
      });
  }

  private initPageSize(): void {
    const pageSize: number = this.paginatorService.lastPageSize(LocalStorageKey.ROLES_ITEM_PER_PAGE);
    this.criteria.searchPage.pageSize = pageSize;
    this.paginator.pageSize = pageSize;
    this.cdr.detectChanges();
  }

  private afterChangePaginatorFields(pageEvent: PageEvent): void {
    const pageSize: number = pageEvent.pageSize;
    const pageIndex: number = pageEvent.pageIndex;
    if (pageSize !== this.criteria.searchPage.pageSize || pageIndex !== this.criteria.searchPage.pageNumber) {
      this.paginatorService.changeLastPageSize(LocalStorageKey.ROLES_ITEM_PER_PAGE, pageSize);
      this.criteria.searchPage.pageSize = pageSize;
      this.criteria.searchPage.pageNumber = pageIndex;
      this.searchRole();
    }
  }

}

interface RoleTableModel extends RoleDTO {
  authorityTranslateModels: AuthorityTranslateModel[];
}

interface AuthoritySelectModel {
  authority: Authority;
  translate: string;
}
