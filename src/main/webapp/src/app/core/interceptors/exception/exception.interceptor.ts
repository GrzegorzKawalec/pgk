import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable, Injector} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {RouteSignIn} from '../../../common/const/routes';
import {ExceptionInfoBodyComponent} from './exception-info-body.component';
import {ExceptionInfoModel} from './exception-info.model';

@Injectable({
  providedIn: 'root'
})
export class ExceptionInterceptor implements HttpInterceptor {

  private readonly router: Router;
  private readonly snackBar: MatSnackBar;
  private readonly translateService: TranslateService;

  constructor(
    injector: Injector
  ) {
    this.router = injector.get(Router);
    this.snackBar = injector.get(MatSnackBar);
    this.translateService = injector.get(TranslateService);
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(catchError(ex => {
      let handled: boolean;
      if (this.navigateToSignInPageIfUnAuthorize(ex)) {
        handled = true;
      } else {
        const errorModel: ExceptionInfoModel = ExceptionInterceptor.getErrorModel(ex);
        handled = this.showInfo(errorModel);
      }
      return throwError({...ex, handled: handled});
    }));
  }

  private navigateToSignInPageIfUnAuthorize(ex: any): boolean {
    const httpErrorResponse: HttpErrorResponse = ex instanceof HttpErrorResponse ? ex : null;
    if (httpErrorResponse && httpErrorResponse.status === 401) {
      this.router.navigate(RouteSignIn.ROUTE_COMMANDS);
      return true;
    }
    return false;
  }

  private static getErrorModel(ex: any): ExceptionInfoModel {
    const httpErrorResponse: HttpErrorResponse = ex instanceof HttpErrorResponse ? ex : null;
    return new ExceptionInfoModel(httpErrorResponse);
  }

  private showInfo(errorModel: ExceptionInfoModel): boolean {
    if (errorModel) {
      this.snackBar.openFromComponent(ExceptionInfoBodyComponent, {data: errorModel, duration: 5000});
      return true;
    }
    return false;
  }

}
