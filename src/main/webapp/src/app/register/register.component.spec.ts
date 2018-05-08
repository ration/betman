import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {RegisterComponent} from './register.component';
import {FormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {AuthenticationService} from '../authentication.service';
import {UserService} from '../user.service';
import {AlertService} from '../alert.service';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

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

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
