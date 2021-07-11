import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {LegalActAPI} from '../../../common/api/api';
import {LegalActCriteria, LegalActDTO} from '../../../common/api/api-models';
import {Page} from '../../../common/api/api-pagination.models';

@Injectable({
  providedIn: 'root'
})
export class LegalActService {

  constructor(
    private http: HttpClient
  ) {
  }

  findById(legalActId: number): Observable<LegalActDTO> {
    return this.http.get(LegalActAPI.url + '/' + legalActId);
  }

  create(dto: LegalActDTO): Observable<LegalActDTO> {
    return this.http.post(LegalActAPI.url, dto);
  }

  update(dto: LegalActDTO): Observable<LegalActDTO> {
    return this.http.put(LegalActAPI.url, dto);
  }

  find(criteria?: LegalActCriteria): Observable<Page<LegalActDTO>> {
    return this.http.post(LegalActAPI.find, criteria);
  }

}