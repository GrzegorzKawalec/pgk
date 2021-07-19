import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ProjectAPI} from '../../../common/api/api';
import {ParticipantDTO, ProjectCriteria, ProjectDataForUpsertDTO, ProjectDTO, SelectDTO} from '../../../common/api/api-models';
import {Page} from '../../../common/api/api-pagination.models';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  constructor(
    private http: HttpClient
  ) {
  }

  findForUpsertById(projectId: number): Observable<ProjectDataForUpsertDTO> {
    return this.http.get(ProjectAPI.dataForUpsert + '/' + projectId);
  }

  create(dto: ProjectDTO): Observable<ProjectDTO> {
    return this.http.post(ProjectAPI.url, dto);
  }

  update(dto: ProjectDTO): Observable<ProjectDTO> {
    return this.http.put(ProjectAPI.url, dto);
  }

  getParticipants(): Observable<ParticipantDTO[]> {
    return this.http.get<ParticipantDTO[]>(ProjectAPI.participants);
  }

  getSelectLegalActs(): Observable<SelectDTO[]> {
    return this.http.get<SelectDTO[]>(ProjectAPI.selectLegalActs);
  }

  getSelectParticipants(): Observable<SelectDTO[]> {
    return this.http.get<SelectDTO[]>(ProjectAPI.selectParticipants);
  }

  find(criteria?: ProjectCriteria): Observable<Page<ProjectDTO>> {
    return this.http.post(ProjectAPI.find, criteria);
  }

}
