import {ApiFilter} from './ApiFilter';
import {HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {Group} from '../group.model';
import {GroupsService} from '../groups.service';

export class GroupsFilter implements ApiFilter {

  private groups = {};
  private id = 2;

  constructor() {
    this.groups[1] = {id: '1', name: 'Sample group', description: 'this is a group', key: this.makeid()};
  }

  filter(request: HttpRequest<any>): Observable<HttpResponse<any>> {
    if (request.url.match(GroupsService.newGroupUrl) && request.method === 'POST') {
      const group: Group = request.body;
      group.key = this.makeid();
      group.id = this.id++;
      this.groups[group.id] = group;
      return Observable.of(new HttpResponse({status: 200, body: group}));
    }
    if (request.url.match(GroupsService.getGroupUrl) && request.method === 'GET') {
      return Observable.of(new HttpResponse({status: 200, body: [this.groups[1]]}));
    }
    return undefined;
  }


  private makeid() {
    let text = '';
    const possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

    for (let i = 0; i < 32; i++) {
      text += possible.charAt(Math.floor(Math.random() * possible.length));
    }

    return text;
  }

}
