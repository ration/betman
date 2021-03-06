import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BettingComponent} from './betting.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {GamesService} from '../games.service';
import {AuthenticationService} from '../authentication.service';
import {AlertService} from '../alert.service';
import {GroupsService} from '../groups.service';
import {Match, Team} from '../game.model';
import {of} from 'rxjs/internal/observable/of';
import {Group} from '../group.model';

const germany: Team = {id: 1, iso: 'ge', name: 'Germany'};
const england: Team = {id: 2, iso: 'gb', name: 'England'};

describe('BettingComponent', () => {
  let component: BettingComponent;
  let fixture: ComponentFixture<BettingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BettingComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule, FormsModule, RouterTestingModule.withRoutes([])],
      providers: [GamesService, AuthenticationService, AlertService, GroupsService]
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

  it('allow edit', () => {
    const date1 = new Date();
    date1.setHours(date1.getHours() - 2);
    const authService = TestBed.get(AuthenticationService);
    component.user = 'user';
    spyOn(authService, 'currentUser').and.returnValue({name: 'user'});
    const match1: Match = {
      'id': 1,
      home: germany.id,
      away: england.id,
      date: date1.toISOString(),
      description: 'preliminary'
    };
    expect(component.canBet(match1)).toBe(false);
    date1.setHours(date1.getHours() + 4);
    const match2: Match = {
      'id': 1,
      home: germany.id,
      away: england.id,
      date: date1.toISOString(),
      description: 'preliminary'
    };
    expect(component.canBet(match2)).toBe(true);

  });

  it('style change', () => {

    const groupService = TestBed.get(GroupsService);
    const group: Group = {name: 'some', description: 'description', game: 'somegame'};
    spyOn(groupService, 'get').and.returnValue(of(group));
    spyOn(groupService, 'all').and.returnValue(of([group]));

    const match: Match = {
      id: 1,
      home: germany.id,
      away: england.id,
      date: Date(),
      description: 'preliminary',
      homeGoals: 0,
      awayGoals: 1,
    };
    component.bets.scores.push({id: 1, home: 0, away: 0});
    component.updateLookup();
    expect(component.style(match)).toBe('partial');
    match.awayGoals = 0;
    expect(component.style(match)).toBe('exact');
  });


  it('cant edit winner/goalKing on started', () => {
    expect(component.started()).toBe(false);
    const future: Date = new Date();
    future.setDate(future.getDate() + 1);
    const past: Date = new Date();
    past.setDate(past.getDate() - 1);
    const match = {
      id: 1,
      home: germany.id,
      away: england.id,
      date: future.toISOString(),
      description: 'preliminary',
      homeGoals: 0,
      awayGoals: 1,
    };
    const match2 = {
      id: 1,
      home: germany.id,
      away: england.id,
      date: future.toISOString(),
      description: 'preliminary',
      homeGoals: 0,
      awayGoals: 1,
    };
    component.game = {id: -1, name: 'some', description: 'some', matches: [match, match2], teams: [germany, england]};
    expect(component.started()).toBe(false);
    match.date = past.toISOString();
    component.game = {id: -1, name: 'some', description: 'some', matches: [match, match2], teams: [germany, england]};
    expect(component.started()).toBe(true);

  });
})
;
