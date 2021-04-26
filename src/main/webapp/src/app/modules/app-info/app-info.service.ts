import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {InfoAPI} from '../../core/api/api';
import {InfoBasicDTO} from '../../core/api/api-models';

@Injectable({
  providedIn: 'root'
})
export class AppInfoService {

  constructor(
    private http: HttpClient
  ) {
  }

  getBasicInfo(): Observable<InfoBasicDTO> {
    return this.http.get(InfoAPI.basicInfo);
  }

}
