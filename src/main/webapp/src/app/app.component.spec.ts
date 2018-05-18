import {async, TestBed} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {GamesService} from './games.service';
import {UserService} from './user.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AlertService} from './alert.service';
import {RouterTestingModule} from '@angular/router/testing';
import {AlertComponent} from './alert/alert.component';
import {AuthenticationService} from './authentication.service';
import {GroupsService} from './groups.service';

describe('AppComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent, AlertComponent
      ],
      imports: [HttpClientTestingModule, ReactiveFormsModule, FormsModule, RouterTestingModule.withRoutes([])],
      providers: [GamesService, UserService, AlertService, AuthenticationService, GroupsService ]
    }).compileComponents();
  }));
  it('should create the Betman', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));
  it(`should have as title 'Betman'`, async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('Betman');
  }));


});
