import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {UserComponent} from './user.component';
import {FormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {UserService} from '../user.service';
import {AlertService} from '../alert.service';
import {AuthenticationService} from '../authentication.service';
import {User} from '../user.model';

describe('UserComponent', () => {
  let component: UserComponent;
  let fixture: ComponentFixture<UserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [UserComponent],
      imports: [FormsModule, HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [UserService, AlertService]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserComponent);
    component = fixture.componentInstance;
  });

  it('Initialization', () => {
    const user: User = {name: 'some', admin: false, password: 'pass'};

    const alertService = TestBed.get(AuthenticationService);
    spyOn(alertService, 'currentUser').and.returnValue(user);
    expect(component).toBeTruthy();
  });
});
