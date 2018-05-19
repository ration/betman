import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {JoinComponent} from './join.component';
import {FormsModule} from '@angular/forms';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {GroupsService} from '../groups.service';
import {Group} from '../group.model';
import {ActivatedRoute} from '@angular/router';
import {Observable, of} from 'rxjs';
import {AuthenticationService} from '../authentication.service';

const KEY = 'somekey';


describe('JoinComponent', () => {
  let component: JoinComponent;
  let fixture: ComponentFixture<JoinComponent>;


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [JoinComponent],
      imports: [FormsModule, RouterTestingModule.withRoutes([{path: 'join/:key', component: JoinComponent}]), HttpClientTestingModule],
      providers: [GroupsService, AuthenticationService
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

  it('should fetch game data on link', () => {
    const groupService = TestBed.get(GroupsService);
    const group: Group = {name: 'name', description: 'description', game: 'game', key: KEY};
    expect(component.key).toBe(KEY);
    spyOn(groupService, 'all').and.returnValue(of([]));

    const spy = spyOn(groupService, 'get').and.returnValue(of(group));
    component.ngOnInit();
    expect(spy).toHaveBeenCalledWith(KEY);
  });
});
