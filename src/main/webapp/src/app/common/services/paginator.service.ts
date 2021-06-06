import {Injectable} from '@angular/core';
import {DEFAULT_PAGE_SIZE, PAGE_SIZE_OPTIONS} from '../const/const';
import {LocalStorageKey} from './local-storage/local-storage-key';
import {LocalStorageService} from './local-storage/local-storage.service';

@Injectable({
  providedIn: 'root'
})
export class PaginatorService {

  readonly defaultPageSize: number = DEFAULT_PAGE_SIZE;
  readonly pageSizeOptions: number[] = PAGE_SIZE_OPTIONS;

  constructor(
    private localStorageService: LocalStorageService
  ) {
  }

  lastPageSize(key: LocalStorageKey): number {
    const lastPage: number = this.localStorageService.read(key);
    if (!lastPage) {
      return this.defaultPageSize;
    }
    return this.pageSizeOptions.includes(lastPage) ?
      lastPage : this.defaultPageSize;
  }

  changeLastPageSize(key: LocalStorageKey, pageSize: number): void {
    if (!pageSize || !this.pageSizeOptions.includes(pageSize)) {
      return;
    }
    this.localStorageService.write(key, pageSize);
  }

}
