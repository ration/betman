import {inject, TestBed} from '@angular/core/testing';

import {GroupsService} from './groups.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Group} from './group.model';
import {HttpResponse} from '@angular/common/http';
import {AuthenticationService} from './authentication.service';
import {RouterTestingModule} from '@angular/router/testing';

describe('GroupsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [GroupsService, AuthenticationService]
    });
  });

  it('gets group', inject([HttpTestingController, GroupsService], (httpMock: HttpTestingController, service: GroupsService) => {
    const key = 'mykey';
    const body: Group = {name: 'name', description: 'description', game: 'game', key: key};
    service.get(key).subscribe((ans: Group) => {
      expect(ans).toEqual(body);
    });
    const req = httpMock.expectOne(GroupsService.getGroupInfo + key);
    req.flush(body);
    httpMock.verify();
  }));

  it('gets all groups', inject([HttpTestingController, GroupsService], (httpMock: HttpTestingController, service: GroupsService) => {
    const key = 'mykey';
    const body: Group = {name: 'name', description: 'description', game: 'game', key: key};
    service.all().subscribe((ans: Group[]) => {
      expect(ans[0]).toEqual(body);
    });
    const req = httpMock.expectOne(GroupsService.getGroupUrl);
    req.flush({groups: [body]});
    httpMock.verify();
  }));

  it('crates new group', inject([HttpTestingController, GroupsService], (httpMock: HttpTestingController, service: GroupsService) => {
    const body: Group = {name: 'name', description: 'description', game: 'game'};
    const response: Group = {name: 'name', key: 'myKey', description: 'description', game: 'game'};
    service.newGroup(body).subscribe((ans: Group) => {
      expect(ans.key).toEqual('myKey');
    });
    const req = httpMock.expectOne(GroupsService.newGroupUrl);
    expect(req.request.responseType).toEqual('json');
    req.flush(response);
    httpMock.verify();
  }));

  it('updates group', inject([HttpTestingController, GroupsService], (httpMock: HttpTestingController, service: GroupsService) => {
    const body: Group = {name: 'name', description: 'description', game: 'game'};
    service.update(body).subscribe((ans: HttpResponse<any>) => {
      expect(ans.status).toBe(204);
    });
    const req = httpMock.expectOne(GroupsService.updateGroupUrl);
    req.flush(null);
    httpMock.verify();
  }));


  it('changes display name in group', inject([HttpTestingController, GroupsService], (httpMock: HttpTestingController, service: GroupsService) => {
    const key = 'mykey';
    const displayName = 'newName';
    service.updateDisplayName(key, displayName).subscribe((res: HttpResponse<void>) => {
      expect(res.status).toBe(200);
    });
    const req = httpMock.expectOne(GroupsService.updateGroupDisplayName +
      '?groupKey=mykey&displayName=newName');
    req.flush(new HttpResponse());
    httpMock.verify();
  }));

  it('set active', inject([HttpTestingController, GroupsService], (httpMock: HttpTestingController, service: GroupsService) => {
    service.setActive('some');
    const active = service.getActive();
    expect(active).toBe('some');
  }));

  it('monitor active', inject([HttpTestingController, GroupsService], (httpMock: HttpTestingController, service: GroupsService) => {
    const body: Group = {name: 'name', description: 'description', game: 'game'};

    service.setActive('some');
    const req = httpMock.expectOne(GroupsService.getGroupInfo + 'some');
    req.flush(body);
    service.active().subscribe(value => expect(value).toBe(body));

    httpMock.verify();

  }));

  it('joins group', inject([HttpTestingController, GroupsService], (httpMock: HttpTestingController, service: GroupsService) => {
    const key = 'mykey';
    const body: Group = {name: 'name', description: 'description', game: 'game', key: key};
    const displayName = 'awesome';

    service.join(key, displayName).subscribe(value => expect(value).toBe(body));
    const req = httpMock.expectOne(GroupsService.joinGroupUrl + '?key=' + key + '&displayName=' + displayName);
    req.flush(body);
    httpMock.verify();
  }));

});
