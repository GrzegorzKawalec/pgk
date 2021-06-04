import {Injectable} from '@angular/core';
import {MatPaginatorIntl} from '@angular/material/paginator';
import {TranslateService} from '@ngx-translate/core';
import {Subject} from 'rxjs';

@Injectable()
export class PaginatorIntlService implements MatPaginatorIntl {

  readonly changes: Subject<void> = new Subject<void>();

  firstPageLabel: string = '';
  lastPageLabel: string = '';

  previousPageLabel: string = '';
  nextPageLabel: string = '';

  itemsPerPageLabel: string = '';

  private readonly prefixKey: string = 'pagination.';

  constructor(
    private translateService: TranslateService
  ) {
    this.initLabels();
    this.translateService.onLangChange.subscribe(() => this.initLabels());
  }

  getRangeLabel(page: number, pageSize: number, length: number): string {
    const ofLabel: string = ' ' + this.getOfLabel() + ' ';
    if (length === 0 || pageSize === 0) {
      return 0 + ofLabel + length;
    }
    length = Math.max(length, 0);
    const startIndex: number = page * pageSize;
    const endIndex: number = startIndex < length ? Math.min(startIndex + pageSize, length) : startIndex + pageSize;
    return (startIndex + 1) + ' – ' + endIndex + ofLabel + length;
  }

  private initLabels(): void {
    this.firstPageLabel = this.getFirstPageLabel();
    this.lastPageLabel = this.getLastPageLabel();
    this.previousPageLabel = this.getPreviousPageLabel();
    this.nextPageLabel = this.getNextPageLabel();
    this.itemsPerPageLabel = this.getItemsPerPageLabel() + ':';
  }

  private getFirstPageLabel(): string {
    return this.translateService.instant(this.prefixKey + 'first-page');
  }

  private getLastPageLabel(): string {
    return this.translateService.instant(this.prefixKey + 'last-page');
  }

  private getPreviousPageLabel(): string {
    return this.translateService.instant(this.prefixKey + 'previous-page');
  }

  private getNextPageLabel(): string {
    return this.translateService.instant(this.prefixKey + 'next-page');
  }

  private getItemsPerPageLabel(): string {
    return this.translateService.instant(this.prefixKey + 'items-per-page');
  }

  private getOfLabel(): string {
    return this.translateService.instant(this.prefixKey + 'of');
  }

}
