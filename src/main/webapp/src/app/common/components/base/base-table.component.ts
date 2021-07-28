import {ChangeDetectorRef, Component, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatSnackBar} from '@angular/material/snack-bar';
import {TranslateService} from '@ngx-translate/core';
import {Subject} from 'rxjs';
import {debounceTime, takeUntil} from 'rxjs/operators';
import {BaseCriteria} from '../../api/api-models';
import {LocalStorageKey} from '../../services/local-storage/local-storage-key';
import {PaginatorService} from '../../services/paginator.service';
import {BaseComponent} from '../base.component';

@Component({
  template: ``
})
export abstract class BaseTableComponent<T extends BaseCriteria> extends BaseComponent {

  loading: boolean = false;

  @ViewChild(MatPaginator) protected paginator: MatPaginator;

  protected criteria: T;

  readonly pageSizeOptions: number[] = this.paginatorService.pageSizeOptions;

  filterText: string;
  private applyTextFilter$: Subject<string> = new Subject();

  protected constructor(
    protected translateService: TranslateService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef,
    private paginatorService: PaginatorService,
    private paginLocalStorageKey: LocalStorageKey
  ) {
    super();
  }

  abstract clearFilter(): void;
  protected abstract search(): void

  applyTextFilter(event: KeyboardEvent): void {
    const filterValue: string = (event.target as HTMLInputElement).value;
    this.applyTextFilter$.next(filterValue);
  }

  protected subscribeTextFilter(delay: number = 300): void {
    this.applyTextFilter$
      .pipe(
        takeUntil(this.destroy$),
        debounceTime(delay)
      ).subscribe((filterText: string) => {
      this.criteria.searchBy = filterText;
      this.search();
    });
  }

  protected showSnackBarAfterChangeStatus(messageKey: string, duration: number = 2000): void {
    this.showSnackBar(messageKey, duration);
    this.search();
  }

  protected showSnackBar(messageKey: string, duration: number = 2000): void {
    const message: string = this.translateService.instant(messageKey);
    this.snackBar.open(message, 'OK', {duration: duration});
  }

  protected initPageSize(): void {
    const pageSize: number = this.paginatorService.lastPageSize(this.paginLocalStorageKey);
    this.criteria.searchPage.pageSize = pageSize;
    this.paginator.pageSize = pageSize;
    this.cdr.detectChanges();
  }

  protected subscribePaginator(): void {
    this.paginator.page
      .pipe(takeUntil(this.destroy$))
      .subscribe((pageEvent: PageEvent) => this.afterChangePaginatorFields(pageEvent));
  }

  private afterChangePaginatorFields(pageEvent: PageEvent): void {
    const pageSize: number = pageEvent.pageSize;
    const pageIndex: number = pageEvent.pageIndex;
    if (pageSize !== this.criteria.searchPage.pageSize || pageIndex !== this.criteria.searchPage.pageNumber) {
      this.paginatorService.changeLastPageSize(this.paginLocalStorageKey, pageSize);
      this.criteria.searchPage.pageSize = pageSize;
      this.criteria.searchPage.pageNumber = pageIndex;
      this.search();
    }
  }

}
