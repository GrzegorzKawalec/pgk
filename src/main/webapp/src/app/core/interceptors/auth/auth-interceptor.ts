import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Injectable, Injector} from '@angular/core';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {UserDTO} from '../../../common/api/api-models';
import {UserService} from '../../../common/services/user.service';
import {AuthService} from '../../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {

  private static readonly HEADER_NAME: string = 'Authenticated';

  private readonly authService: AuthService;
  private readonly userService: UserService;

  constructor(
    injector: Injector
  ) {
    this.authService = injector.get(AuthService);
    this.userService = injector.get(UserService)
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req)
      .pipe(
        tap(response => this.handleResponse(response))
      );
  }

  private handleResponse(response: any): void {
    if (response instanceof HttpResponse) {
      this.checkResponseHeader(response);
    }
  }

  private checkResponseHeader(response: HttpResponse<any>): void {
    const authenticated: string = response.headers.get(AuthInterceptor.HEADER_NAME);
    if (!authenticated) {
      return;
    }
    const currentUser: string = this.getCurrentLoggedUser();
    if (!currentUser) {
      return;
    }
    if (authenticated !== currentUser) {
      this.authService.fetchLoggedUserDetails();
    }
  }

  private getCurrentLoggedUser(): string {
    const loggedUser: UserDTO = this.userService.loggedUser;
    return loggedUser ? loggedUser.email : null;
  }

}
