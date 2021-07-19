import {AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatSlideToggleChange} from '@angular/material/slide-toggle';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {Subject} from 'rxjs';
import {debounceTime, finalize, takeUntil} from 'rxjs/operators';
import {Authority, LegalActCriteria, LegalActDTO, UserDTO} from '../../../../common/api/api-models';
import {Page} from '../../../../common/api/api-pagination.models';
import {BaseComponent} from '../../../../common/components/base.component';
import {ModalConfirmComponent} from '../../../../common/components/modal-confirm/modal-confirm.component';
import {ModalConfirmModel} from '../../../../common/components/modal-confirm/modal-confirm.model';
import {RouteProjectManagement} from '../../../../common/const/routes';
import {LocalStorageKey} from '../../../../common/services/local-storage/local-storage-key';
import {PaginatorService} from '../../../../common/services/paginator.service';
import {CriteriaBuilder, DirectionMapper} from '../../../../common/utils/criteria.util';
import {DateTimeUtils} from '../../../../common/utils/date-time.utils';
import {AuthHelper} from '../../../../core/auth/auth.helper';
import {LegalActService} from '../../services/legal-act.service';
import {LegalActDescriptionModalComponent} from './legal-act-description-modal.component';
import {LegalActDetailsModalComponent} from './legal-act-details-modal/legal-act-details-modal.component';

@Component({
  selector: 'pgk-legal-acts',
  templateUrl: './legal-acts.component.html',
  styleUrls: ['./legal-acts.component.scss']
})
export class LegalActsComponent extends BaseComponent implements OnInit, AfterViewInit {

  readonly requiredUpsertAuthorities: Authority[] = [Authority.LEGAL_ACTS_WRITE];
  readonly requiredReadDetailsAuthorities: Authority[] = [...this.requiredUpsertAuthorities, Authority.LEGAL_ACTS_READ];

  readonly prefixTranslateMessage: string = 'project-management.legal-acts.';
  readonly prefixTranslateColumn: string = this.prefixTranslateMessage + 'columns.';

  readonly pageSizeOptions: number[] = this.paginatorService.pageSizeOptions;

  readonly clnName: string = 'name';
  readonly clnDateOf: string = 'date-of';
  readonly clnDescription: string = 'description';
  readonly clnButtons: string = 'buttons';
  readonly displayedColumns: string[] = [this.clnName, this.clnDateOf, this.clnDescription, this.clnButtons];
  tableData: MatTableDataSource<LegalActDTO>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  loading: boolean = false;

  criteria: LegalActCriteria = CriteriaBuilder.init(this.clnName, {isActive: true});

  filterText: string;
  filterDateOfLessOrEqual: Date;
  filterDateOfGreaterOrEqual: Date;
  private applyTextFilter$: Subject<string> = new Subject();

  constructor(
    private router: Router,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef,
    private authHelper: AuthHelper,
    private legalActService: LegalActService,
    private translateService: TranslateService,
    private paginatorService: PaginatorService
  ) {
    super();
  }

  ngOnInit(): void {
    this.subscribeTextFilter();
    this.searchLegalAct();
  }

  ngAfterViewInit(): void {
    this.initPageSize();
    this.subscribeSort();
    this.subscribePaginator();
  }

  clickAddLegalAct(): void {
    this.router.navigate(RouteProjectManagement.LEGAL_ACTS_UPSERT_COMMANDS);
  }

  clickDescription(legalAct: LegalActDTO): void {
    if (legalAct && legalAct.description) {
      this.dialog.open(LegalActDescriptionModalComponent, {data: legalAct});
    }
  }

  clickLink(legalAct: LegalActDTO): void {
    window.open(legalAct.link, '_blank');
  }

  clickDetails(legalAct: LegalActDTO): void {
    this.dialog.open(LegalActDetailsModalComponent, {data: legalAct, minWidth: '750px'});
  }

  clickEdit(legalAct: LegalActDTO): void {
    if (this.authHelper.hasAuthorities(this.requiredUpsertAuthorities)) {
      this.router.navigate([...RouteProjectManagement.LEGAL_ACTS_UPSERT_COMMANDS, legalAct.id]);
    }
  }

  clickDeactivate(legalAct: LegalActDTO): void {
    this.changeLegalActStatus(legalAct, true);
  }

  clickActivate(legalAct: LegalActDTO): void {
    this.changeLegalActStatus(legalAct, false);
  }

  clearFilter(): void {
    if (!this.criteria.searchBy && !this.criteria.dateOfLessThanOrEqual && !this.criteria.dateOfGreaterThanOrEqual) {
      return;
    }

    this.filterText = null;
    this.filterDateOfLessOrEqual = null;
    this.filterDateOfGreaterOrEqual = null;

    this.criteria.searchBy = null;
    this.criteria.dateOfLessThanOrEqual = null;
    this.criteria.dateOfGreaterThanOrEqual = null;

    this.searchLegalAct();
  }

  applyDateOfGreaterOrEqualFilter(): void {
    const date: string = !this.filterDateOfGreaterOrEqual ? null : DateTimeUtils.dateToString(this.filterDateOfGreaterOrEqual);
    if (date === this.criteria.dateOfGreaterThanOrEqual) {
      return;
    }
    this.criteria.dateOfGreaterThanOrEqual = date;
    this.searchLegalAct();
  }

  applyDateOfLessOrEqualFilter(): void {
    const date: string = !this.filterDateOfLessOrEqual ? null : DateTimeUtils.dateToString(this.filterDateOfLessOrEqual);
    if (date === this.criteria.dateOfLessThanOrEqual) {
      return;
    }
    this.criteria.dateOfLessThanOrEqual = date;
    this.searchLegalAct();
  }

  applyInactiveFilter(inactive: MatSlideToggleChange): void {
    this.criteria.isActive = !inactive.checked;
    this.searchLegalAct();
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
        this.searchLegalAct();
    });
  }

  private initPageSize(): void {
    const pageSize: number = this.paginatorService.lastPageSize(LocalStorageKey.LEGAL_ACTS_PER_PAGE);
    this.criteria.searchPage.pageSize = pageSize;
    this.paginator.pageSize = pageSize;
    this.cdr.detectChanges();
  }

  private subscribeSort(): void {
    this.sort.sortChange
      .pipe(takeUntil(this.destroy$))
      .subscribe((sort: Sort) => {
        const sortProperty: string = this.mapSortProperty(sort);
        if (!sortProperty) {
          this.criteria.searchPage.sorting = null;
        } else {
          this.criteria.searchPage.sorting[0].direction = DirectionMapper.map(sort.direction);
          this.criteria.searchPage.sorting[0].property = sortProperty;
        }
        this.searchLegalAct();
      });
  }

  private mapSortProperty(sort: Sort): string {
    if (!sort || !sort.active) {
      return null;
    }
    if (sort.active === this.clnDateOf) {
      return 'dateOf';
    }
    return sort.active;
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
      this.searchLegalAct();
    }
  }

  private searchLegalAct(): void {
    this.loading = true;
    this.legalActService.find(this.criteria)
      .pipe(finalize(() => this.loading = false))
      .subscribe((page: Page<LegalActDTO>) => {
        if (page) {
          this.tableData = new MatTableDataSource(page.content);
          this.paginator.length = page.totalElements;
        }
      });
  }

  private changeLegalActStatus(legalAct: LegalActDTO, forDeactivate: boolean): void {
    if (!this.authHelper.hasAuthorities(this.requiredUpsertAuthorities)) {
      return;
    }
    const confirmModel: ModalConfirmModel = forDeactivate ?
      this.prepareModelForConfirmDeactivateModal(legalAct) :
      this.prepareModelForConfirmActivateModal(legalAct);
    const dialogRef: MatDialogRef<ModalConfirmComponent> = this.dialog.open(ModalConfirmComponent, {data: confirmModel});
    dialogRef.afterClosed()
      .pipe(takeUntil(this.destroy$))
      .subscribe(confirmed => {
        if (confirmed === true) {
          forDeactivate ? this.deactivateLegalAct(legalAct.id) : this.activateLegalAct(legalAct.id);
        }
      });
  }

  private prepareModelForConfirmDeactivateModal(user: UserDTO): ModalConfirmModel {
    return this.prepareModelForConfirmForChangeLegalActStatus(user, 'want-to-deactivate-legal-act');
  }

  private prepareModelForConfirmActivateModal(user: UserDTO): ModalConfirmModel {
    return this.prepareModelForConfirmForChangeLegalActStatus(user, 'want-to-activate-legal-act');
  }

  private prepareModelForConfirmForChangeLegalActStatus(legalAct: LegalActDTO, suffixTitleKey: string): ModalConfirmModel {
    const content: string = legalAct.name + ' (' + legalAct.dateOfStr + ')';
    return {
      titleTranslateKey: this.prefixTranslateMessage + suffixTitleKey,
      showDefaultContent: false,
      content: content
    };
  }

  private deactivateLegalAct(legalActId: number): void {
    this.loading = true;
    this.legalActService.deactivate(legalActId)
      .subscribe(() => this.showSnackBarAfterChangeUserStatus('common.deactivated'));
  }

  private activateLegalAct(legalActId: number): void {
    this.loading = true;
    this.legalActService.activate(legalActId)
      .subscribe(() => this.showSnackBarAfterChangeUserStatus('common.activated'));
  }

  private showSnackBarAfterChangeUserStatus(messageKey: string): void {
    const message: string = this.translateService.instant(messageKey);
    this.snackBar.open(message, 'OK', {duration: 2000});
    this.searchLegalAct();
  }

}
