import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {BehaviorSubject, Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {AuthAPI, UserAPI} from '../api/api';
import {UserDTO} from '../api/api-models';
import {ROUTE_SIGN_IN} from '../const/routes';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private _loggedUser: BehaviorSubject<UserDTO> = new BehaviorSubject(null);

  get loggedUser(): UserDTO {
    return this._loggedUser.value;
  }

  get loggedUser$(): Observable<UserDTO> {
    return this._loggedUser.asObservable();
  }

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
  }

  signOut(): void {
    this.http.post(AuthAPI.signOut, {}).subscribe(() => {
      this._loggedUser.next(null);
      this.router.navigate([ROUTE_SIGN_IN]);
    });
  }

  signIn(username: string, password: string): Observable<void> {
    const data: string = 'email=' + encodeURIComponent(username) + '&password=' + encodeURIComponent(password);
    const headers: HttpHeaders = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');
    return this.http.post<void>(AuthAPI.signIn, data, {headers})
      .pipe(
        tap(() => this.getLoggedUserDetails())
      );
  }

  private getLoggedUserDetails(): void {
    this.http.get(UserAPI.me).subscribe((user: UserDTO) => {
      this._loggedUser.next(user);
    });
  }

}
