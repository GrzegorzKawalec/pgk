import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AuthService} from '../../core/auth/auth.service';
import {UserDTO} from '../api/api-models';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private authService: AuthService
  ) {
  }

  get loggedUser(): UserDTO {
    return this.authService.loggedUser;
  }

  get loggedUser$(): Observable<UserDTO> {
    return this.authService.loggedUser$;
  }

}
