import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {GroupComponent} from './group.component';
import {RouterTestingModule} from '@angular/router/testing';
import {GroupsService} from '../groups.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {AuthenticationService} from '../authentication.service';
import {UserService} from '../user.service';
import {Group} from '../group.model';
import {of} from 'rxjs';
import {FormsModule} from '@angular/forms';
import {AlertService} from '../alert.service';
import {ActivatedRoute} from '@angular/router';


describe('GroupComponent', () => {
  let component: GroupComponent;
  let fixture: ComponentFixture<GroupComponent>;
  let groupService: GroupsService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GroupComponent],
      imports: [FormsModule, RouterTestingModule.withRoutes([]), HttpClientTestingModule],
      providers: [GroupsService, UserService, AlertService, AuthenticationService,
        [GroupComponent, {
          provide: ActivatedRoute,
          useValue: {snapshot: {params: {'group': '123'}}}
        }]]
    }).compileComponents();

  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should load user', () => {
    groupService = TestBed.get(GroupsService);
    const saveName = 'saved name';
    const group: Group = {
      name: 'Sample group',
      description: 'this is a group',
      game: 'Some',
      userDisplayName: saveName
    };
    spyOn(groupService, 'get').and.returnValue(of(group));
    component.ngOnInit();
    expect(component.userDisplayName).toBe(saveName);
  });
})
;
