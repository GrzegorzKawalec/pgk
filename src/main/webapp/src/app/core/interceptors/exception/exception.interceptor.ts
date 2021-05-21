import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable, Injector} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';
import {TranslateService} from '@ngx-translate/core';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {ExceptionInfoBodyComponent} from './exception-info-body.component';
import {ExceptionInfoModel} from './exception-info.model';

@Injectable({
  providedIn: 'root'
})
export class ExceptionInterceptor implements HttpInterceptor {

  private readonly snackBar: MatSnackBar;
  private readonly translateService: TranslateService;

  constructor(
    injector: Injector
  ) {
    this.snackBar = injector.get(MatSnackBar);
    this.translateService = injector.get(TranslateService);
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(catchError(ex => {
      const errorModel: ExceptionInfoModel = ExceptionInterceptor.getErrorModel(ex);
      this.showInfo(errorModel);
      return throwError({...ex, handled: true});
    }));
  }

  private static getErrorModel(ex: any): ExceptionInfoModel {
    const httpErrorResponse: HttpErrorResponse = ex instanceof HttpErrorResponse ? ex : null;
    return new ExceptionInfoModel(httpErrorResponse);
  }

  private showInfo(errorModel: ExceptionInfoModel): void {
    this.snackBar.openFromComponent(ExceptionInfoBodyComponent, {data: errorModel, duration: 5000});
  }

}
