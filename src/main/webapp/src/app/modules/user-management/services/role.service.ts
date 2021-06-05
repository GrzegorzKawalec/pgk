import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';
import {RoleAPI} from '../../../common/api/api';
import {Authority, RoleAuditingDTO, RoleCriteria, RoleDTO} from '../../../common/api/api-models';
import {Page} from '../../../common/api/api-pagination.models';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  constructor(
    private http: HttpClient
  ) {
  }

  getAllAuthorities(): Observable<Authority[]> {
    return this.http.get<Authority[]>(RoleAPI.authorities);
  }

  existsRoleName(roleName: string, roleId?: number): Observable<boolean> {
    roleName = roleName ? roleName.trim() : '';
    if (roleName.length < 1) {
      return of(false);
    }
    const params: HttpParams = new HttpParams()
      .set(RoleAPI.existsNameParamName, roleName)
      .set(RoleAPI.existsNameParamId, roleId ? roleId : '');
    return this.http.get<boolean>(RoleAPI.existsName, {params});
  }

  findById(roleId: number): Observable<RoleDTO> {
    return this.http.get(RoleAPI.url + '/' + roleId);
  }

  create(dto: RoleDTO): Observable<RoleDTO> {
    return this.http.post(RoleAPI.url, dto);
  }

  update(dto: RoleDTO): Observable<RoleDTO> {
    return this.http.put(RoleAPI.url, dto);
  }

  delete(roleId: number): Observable<void> {
    const url: string = RoleAPI.url + '/' + roleId;
    return this.http.delete<void>(url);
  }

  find(criteria?: RoleCriteria): Observable<Page<RoleDTO>> {
    return this.http.post(RoleAPI.find, criteria);
  }

  getAuditingInfo(roleId: number): Observable<RoleAuditingDTO> {
    const url: string = RoleAPI.auditingInfo + '/' + roleId;
    return this.http.get(url);
  }

}
