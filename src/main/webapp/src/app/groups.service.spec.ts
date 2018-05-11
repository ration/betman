import {inject, TestBed} from '@angular/core/testing';

import {GroupsService} from './groups.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Group} from './group.model';

describe('GroupsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [GroupsService]
    });
  });

  it('should be created', inject([GroupsService], (service: GroupsService) => {
    expect(service).toBeTruthy();
  }));

  it('crates new group', inject([HttpTestingController, GroupsService], (httpMock: HttpTestingController, service: GroupsService) => {
    const body: Group = {name: 'name', description: 'description'};
    const response: Group = {name: 'name', key: 'myKey', description: 'description'};
    service.newGroup(body).subscribe((ans: Group) => {
      expect(ans.key).toEqual('myKey');
    });
    const req = httpMock.expectOne(GroupsService.newGroupUrl);
    expect(req.request.responseType).toEqual('json');
    req.flush(response);
    httpMock.verify();
  }));

});
