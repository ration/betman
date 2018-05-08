import {inject, TestBed} from '@angular/core/testing';

import {UserService} from './user.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormsModule} from '@angular/forms';
import {RouterTestingModule} from '@angular/router/testing';
import {AuthenticationService} from './authentication.service';

describe('UserService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserService, AuthenticationService],
      imports: [HttpClientTestingModule, FormsModule, RouterTestingModule.withRoutes([])],
    });
  });

  it('should be created', inject([UserService], (service: UserService) => {
    expect(service).toBeTruthy();
  }));
});
