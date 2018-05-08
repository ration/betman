import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {GroupComponent} from './group.component';
import {RouterTestingModule} from '@angular/router/testing';
import {GroupsService} from '../groups.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {AuthenticationService} from '../authentication.service';
import {UserService} from '../user.service';
import {Observable} from 'rxjs/Observable';
import {Group} from '../group.model';
import {User} from '../user';


describe('GroupComponent', () => {
  let component: GroupComponent;
  let fixture: ComponentFixture<GroupComponent>;
  let groupService: GroupsService;
  let authService: AuthenticationService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GroupComponent],
      imports: [RouterTestingModule.withRoutes([]), HttpClientTestingModule],
      providers: [GroupsService, AuthenticationService, UserService]
    }).compileComponents();

  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    groupService = TestBed.get(GroupsService);
    const group: Group = {name: 'Sample group', description: 'this is a group'};
    spyOn(groupService, 'get').and.returnValue(Observable.of(group));
    authService = TestBed.get(AuthenticationService);
    const user: User = {username: 'username'};
    spyOn(authService, 'currentUser').and.returnValue(user);
    expect(component).toBeTruthy();
  });
})
;
