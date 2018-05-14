import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from '../environments/environment';
import {Group} from './group.model';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';

@Injectable()
export class GroupsService {
  public static readonly newGroupUrl = environment.host + '/api/groups/new';
  public static readonly getGroupUrl = environment.host + '/api/groups/';
  public static readonly updateGroupDisplayName = environment.host + '/api/groups/updateDisplayName';


  constructor(private http: HttpClient) {
  }

  newGroup(group: Group): Observable<Group> {
    return this.http.post<Group>(GroupsService.newGroupUrl, group);
  }

  get(id: string): Observable<Group> {
    return this.http.get<Group>(GroupsService.getGroupUrl + id);
  }

  getGroups(ids: number[]): Observable<Group[]> {
    const urlParam = '[' + ids.join(',') + ']';
    return this.http.get<Group[]>(GroupsService.newGroupUrl + urlParam);
  }

  updateDisplayName(key: string, displayName: string): Observable<HttpResponse<any>> {
    const params = new HttpParams().set('groupKey', key).set('displayName', displayName);
    return this.http.post(GroupsService.updateGroupDisplayName, null, {params, observe: 'response'});
  }
}
