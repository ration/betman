import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {environment} from '../environments/environment';
import {Group, Groups} from './group.model';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {map} from 'rxjs/operators';

@Injectable()
export class GroupsService {
  public static readonly newGroupUrl = environment.host + '/api/groups/new';
  public static readonly getGroupUrl = environment.host + '/api/groups/';
  public static readonly getGroupInfo = environment.host + '/api/groups/info/';
  public static readonly joinGroupUrl = environment.host + '/api/groups/join';


  public static readonly updateGroupDisplayName = environment.host + '/api/groups/updateDisplayName';
  private activeSubject: BehaviorSubject<Group> = new BehaviorSubject(null);


  constructor(private http: HttpClient) {
    if (this.getActive()) {
      this.get(this.getActive()).subscribe(value => this.activeSubject.next(value));
    }
  }

  active(): Observable<Group> {
    return this.activeSubject.asObservable();
  }

  setActive(key: string) {
    localStorage.setItem('activeGroup', key);
    this.get(key).subscribe(value => this.activeSubject.next(value));
  }

  newGroup(group: Group): Observable<Group> {
    return this.http.post<Group>(GroupsService.newGroupUrl, group);
  }

  all(): Observable<Group[]> {
    return this.http.get<Groups>(GroupsService.getGroupUrl).pipe(map((res: Groups) => res.groups));
  }

  get(id: string): Observable<Group> {
    return this.http.get<Group>(GroupsService.getGroupInfo + id);
  }

  getGroups(ids: number[]): Observable<Group[]> {
    const urlParam = '[' + ids.join(',') + ']';
    return this.http.get<Group[]>(GroupsService.newGroupUrl + urlParam);
  }

  updateDisplayName(key: string, displayName: string): Observable<HttpResponse<any>> {
    const params = new HttpParams().set('groupKey', key).set('displayName', displayName);
    return this.http.post(GroupsService.updateGroupDisplayName, null, {params, observe: 'response'});
  }

  getActive(): string {
    return localStorage.getItem('activeGroup');
  }

  join(key: string, displayName: string): Observable<Group> {
    const params = new HttpParams().set('key', key).set('displayName', displayName);
    return this.http.post<Group>(GroupsService.joinGroupUrl, null, {params});
  }
}
