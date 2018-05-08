import {inject, TestBed} from '@angular/core/testing';

import {AuthGuard} from './auth.guard';
import {RouterTestingModule} from '@angular/router/testing';

describe('AuthGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AuthGuard],
      imports: [RouterTestingModule.withRoutes([])]
    });
  });

  it('should ...', inject([AuthGuard], (guard: AuthGuard) => {
    expect(guard).toBeTruthy();
  }));
});
