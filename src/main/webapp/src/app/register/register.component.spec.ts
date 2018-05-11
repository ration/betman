import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {RegisterComponent} from './register.component';
import {FormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {AuthenticationService} from '../authentication.service';
import {UserService} from '../user.service';
import {AlertService} from '../alert.service';
import {User} from '../user';
import {of} from 'rxjs/internal/observable/of';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let userService: UserService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [HttpClientTestingModule, FormsModule, RouterTestingModule.withRoutes([])],
      providers: [UserService, AuthenticationService, AlertService]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('register should create user', () => {
    userService = TestBed.get(UserService);
    const registerSpy = spyOn(userService, 'create');
    registerSpy.and.returnValue(of('OK'));
    const user: User = {name: 'some', password: 'somePass'};
    component.model = user;
    component.register()
    expect(registerSpy).toHaveBeenCalledWith(user);
  });
});
