import {HttpErrorResponse} from '@angular/common/http';
import {ResponseExceptionType} from '../../../common/api/api-models';

export class ExceptionInfoModel {
  readonly type: string;
  readonly uuid: string;
  readonly status: number;

  constructor(ex: HttpErrorResponse) {
    this.status = ExceptionInfoModel.getStatus(ex) || 500;
    this.uuid = ExceptionInfoModel.getErrorUUID(ex) || '';
    this.type = ExceptionInfoModel.getErrorType(ex) || 'UNEXPECTED';
  }

  private static getStatus(ex: HttpErrorResponse): number {
    return ex && ex.error && ex.error.httpStatus ? ex.error.httpStatus : 500;
  }

  private static getErrorUUID(ex: HttpErrorResponse): string {
    return ex && ex.error ? ex.error.errorUUID || '' : '';
  }

  private static getErrorType(ex: HttpErrorResponse): string {
    if (!ex && !ex.error) {
      return '';
    }
    const type: any = ex.error.type;
    if (!type) {
      return '';
    }
    return Object.values(ResponseExceptionType).includes(type) ? type : '';
  }

}
