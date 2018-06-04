import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {LoginComponent} from './login.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {AuthenticationService} from '../authentication.service';
import {UserService} from '../user.service';
import {AlertService} from '../alert.service';
import {ActivatedRoute} from '@angular/router';
import {GroupsService} from '../groups.service';
import {IntroductionComponent} from '../introduction/introduction.component';

const KEY = 'somekey';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [LoginComponent, IntroductionComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule, FormsModule, RouterTestingModule.withRoutes([
        {path: 'login', component: LoginComponent}])],
      providers: [UserService, AuthenticationService, AlertService, GroupsService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              params: {key: KEY},
              queryParams: {returnUrl: ''}
            }
          }
        }]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('login with given key', () => {
    component.ngOnInit();
    expect(component.key).toBe(KEY);
  });
});
