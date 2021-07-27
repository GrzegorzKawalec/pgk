import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {DashboardAPI} from '../../../common/api/api';
import {DashboardDTO} from '../../../common/api/api-models';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(
    private http: HttpClient
  ) {
  }

  getDashboard(): Observable<DashboardDTO> {
    return this.http.get(DashboardAPI.url);
  }

}
