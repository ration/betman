import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {JoinComponent} from './join.component';
import {FormsModule} from '@angular/forms';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {GroupsService} from '../groups.service';
import {Group} from '../group.model';
import {ActivatedRoute} from '@angular/router';
import {of} from 'rxjs';
import {AuthenticationService} from '../authentication.service';
import {LoginComponent} from '../login/login.component';
import {GroupComponent} from '../group/group.component';
import {IntroductionComponent} from '../introduction/introduction.component';
import {AlertService} from '../alert.service';

const KEY = 'somekey';
const group: Group = {name: 'name', description: 'description', game: 'game', key: KEY};

describe('JoinComponent', () => {
  let component: JoinComponent;
  let fixture: ComponentFixture<JoinComponent>;


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [JoinComponent, LoginComponent, GroupComponent, IntroductionComponent],
      imports: [FormsModule, RouterTestingModule.withRoutes([{path: 'join/:key', component: JoinComponent},
        {path: 'login/:key', component: LoginComponent},
        {path: 'group/:key', component: GroupComponent}]), HttpClientTestingModule],
      providers: [GroupsService, AuthenticationService, AlertService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              params: {key: KEY}
            }
          }
        }]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JoinComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  beforeEach(() => {
    spyOn(TestBed.get(AuthenticationService), 'currentUser').and.returnValue({name: 'some'});
  });

  it('should fetch game data on link', () => {

    const groupService = TestBed.get(GroupsService);
    expect(component.key).toBe(KEY);
    const spy = spyOn(groupService, 'get').and.returnValue(of(group));
    component.ngOnInit();
    expect(spy).toHaveBeenCalledWith(KEY);
  });


  it('join should send ', () => {
    const groupService = TestBed.get(GroupsService);
    component.key = 'somekey';
    component.name = 'myname';
    const spy = spyOn(groupService, 'join').and.returnValue(of(group));
    component.join();
    expect(spy).toHaveBeenCalled();
  });
});
