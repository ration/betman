import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {environment} from '../environments/environment';
import {Group} from './group.model';
import {HttpClient, HttpParams} from '@angular/common/http';

@Injectable()
export class GroupsService {
  public static readonly newGroupUrl = environment.host + '/api/groups/new';
  public static readonly getGroupUrl = environment.host + '/api/groups/';


  constructor(private http: HttpClient) {
  }

  newGroup(group: Group): Observable<Group> {
    return this.http.post<Group>(GroupsService.newGroupUrl, group);
  }

  get(id: number): Observable<Group> {
    const params = new HttpParams().set('group', id.toString());
    return this.http.get<Group>(GroupsService.newGroupUrl, {params});
  }

  getGroups(ids: number[]): Observable<Group[]> {
    const urlParam = '[' + ids.join(',') + ']';
    return this.http.get<Group[]>(GroupsService.newGroupUrl + urlParam);
  }

}
