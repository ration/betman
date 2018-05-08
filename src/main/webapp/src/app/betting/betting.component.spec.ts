import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BettingComponent} from './betting.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {GamesService} from '../games.service';
import {AuthenticationService} from '../authentication.service';
import {AlertService} from '../alert.service';

describe('BettingComponent', () => {
  let component: BettingComponent;
  let fixture: ComponentFixture<BettingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BettingComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule, FormsModule, RouterTestingModule.withRoutes([])],
      providers: [GamesService, AuthenticationService, AlertService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BettingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
