import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HomeComponent} from './home.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {UserService} from '../user.service';
import {AuthenticationService} from '../authentication.service';
import {GroupsService} from '../groups.service';
import {Group} from '../group.model';
import {of} from 'rxjs/internal/observable/of';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HomeComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule, FormsModule, RouterTestingModule.withRoutes([])],
      providers: [UserService, AuthenticationService, GroupsService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('fetch group options', () => {
    const groupService = TestBed.get(GroupsService);
    const group: Group = {
      name: 'Sample group',
      description: 'this is a group',
      game: 'Some',
      userDisplayName: 'displayName'
    };
    spyOn(groupService, 'all').and.returnValue(of([group]));
    component.ngOnInit();
    expect(component.groups[0].name).toBe(group.name);
  });
});
