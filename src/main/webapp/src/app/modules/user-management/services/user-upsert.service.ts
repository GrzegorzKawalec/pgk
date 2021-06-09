import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';
import {UserAPI} from '../../../common/api/api';
import {UserUpsertDTO} from '../../../common/api/api-models';

@Injectable({
  providedIn: 'root'
})
export class UserUpsertService {

  constructor(
    private http: HttpClient
  ) {
  }

  existsUserEmail(userEmail: string, userId?: number): Observable<boolean> {
    userEmail = userEmail ? userEmail.trim() : '';
    if (userEmail.length < 1) {
      return of(false);
    }
    const params: HttpParams = new HttpParams()
      .set(UserAPI.existsEmailParamEmail, userEmail)
      .set(UserAPI.existsEmailParamId, userId ? userId : '');
    return this.http.get<boolean>(UserAPI.existsEmail, {params});
  }

  findUpsertById(userId: number): Observable<UserUpsertDTO> {
    const url: string = UserAPI.findUserUpsert + '/' + userId;
    return this.http.get(url);
  }

  create(dto: UserUpsertDTO): Observable<UserUpsertDTO> {
    return this.http.post(UserAPI.url, dto);
  }

  update(dto: UserUpsertDTO): Observable<UserUpsertDTO> {
    return this.http.put(UserAPI.url, dto);
  }

}
