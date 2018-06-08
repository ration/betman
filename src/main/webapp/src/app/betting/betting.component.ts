import {Component, OnInit} from '@angular/core';
import {Bet, ScoreBet} from '../bet.model';
import {GamesService} from '../games.service';
import {Observable, of, Subject} from 'rxjs';


import {AuthenticationService} from '../authentication.service';
import {Game, Match, Team} from '../game.model';
import {AlertService} from '../alert.service';
import {debounceTime, flatMap, map} from 'rxjs/operators';
import {GroupsService} from '../groups.service';
import {Group} from '../group.model';
import {ActivatedRoute} from '@angular/router';
import {a} from '@angular/core/src/render3';

@Component({
  selector: 'app-betting',
  templateUrl: './betting.component.html',
  styleUrls: ['./betting.component.css']
})
export class BettingComponent implements OnInit {

  game: Game = {id: -1, name: '', matches: [], description: '', teams: []};
  bets: Bet = {scores: []};
  teams: Map<number, Team> = new Map<number, Team>();
  displayName: string = null;
  group: Group = null;


  private saveSubject: Subject<Bet> = new Subject<Bet>();
  user = '';
  private lookup = new Map<number, ScoreBet>();

  constructor(private gamesService: GamesService,
              private authService: AuthenticationService,
              private alertService: AlertService,
              private groupService: GroupsService,
              private route: ActivatedRoute) {
    const key = this.groupService.getActive();

    if (key) {
      this.bets.groupKey = key;
      this.groupService.get(key).pipe(
        flatMap((group: Group) => {
          this.group = group;
          return this.gamesService.all(group.game);
        })
      ).subscribe((data: Game) => {
        for (const team of data.teams) {
          this.teams[team.id] = team;
        }
        this.game = data;

        this.getBettingData();
      });
    }

    this.saveSubject.pipe(debounceTime(2000)).subscribe(value => {
      this.onSubmit(value);
    });
    if (this.authService.currentUser() != null) {
      this.user = this.authService.currentUser().name;
    }
  }

  private static getRandomGoals(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }

  guess() {
    if (confirm('Are you sure? This will overwrite all of your choices?')) {
      this.gamesService.guess(this.bets.groupKey, this.bets).subscribe(value => {
        this.bets = value;
        this.updateLookup();
        this.alertService.success('Saved', false, 2000);
      });
    }
  }

  canBet(match: Match): boolean {
    return this.ownBets() && !this.matchStarted(match);
  }

  matchStarted(match: Match) {
    const now = new Date();
    return new Date(Date.parse(match.date)) <= now;
  }

  ownBets(): boolean {
    return this.authService.currentUser() != null && this.user === this.authService.currentUser().name;
  }

  onChange() {
    this.saveSubject.next(this.bets);
  }

  onSubmit(value: Bet) {
    this.gamesService.saveBet(this.bets.groupKey, value).subscribe(() => {
      this.alertService.success('Saved', false, 2000);
    });
  }

  winnerSelect(id: number) {
    this.bets.winner = id;
    this.onSubmit(this.bets);
  }

  updateLookup() {
    for (const score of this.bets.scores) {
      this.lookup[score.id] = score;
    }
  }

  style(match: Match): string {
    if (match.homeGoals != null && match.awayGoals != null) {
      const home = match.homeGoals === this.getScore(match.id).home;
      const away = match.awayGoals === this.getScore(match.id).away;
      const winner = this.winnerCorrect(match);

      if (home && away) {
        return 'exact';
      } else if (winner && (home || away)) {
        return 'winnerPartial';
      }
      if (winner || home || away) {
        return'partial';
      }
    }
    return '';
  }


  winnerCorrect(match: Match) {
    if (match.homeGoals != null && match.awayGoals != null) {
      const result = match.homeGoals - match.awayGoals;
      const betResult = this.getScore(match.id).home - this.getScore(match.id).away;
      return ((result < 0 && betResult < 0) ||
        (result === 0 && betResult === 0) ||
        (result > 0 && betResult > 0));

    }
    return false;
  }

  canSee(match: Match) {
    if (this.ownBets()) {
      return true;
    }
    return this.matchStarted(match);
  }

  ngOnInit() {
  }

  getScore(id: number): ScoreBet {
    return this.lookup[id];
  }

  getTeam(id: number): Team {
    if (this.teams[id]) {
      return this.teams[id];
    } else {
      return {name: '???', iso: 'xx', id: -1};
    }

  }

  teamsByName(): Team[] {
    return this.game.teams.sort((a, b) => a.name.localeCompare(b.name));
  }

  private getBettingData() {
    this.user = this.route.snapshot.params.user || this.authService.currentUser().name;


    for (const game of this.game.matches) {
      const scoreBet = {id: game.id, home: 0, away: 0};
      this.lookup[game.id] = scoreBet;
      this.bets.scores.push(scoreBet);
    }
    this.gamesService.betsForUser(this.bets.groupKey, this.user).subscribe((value: Bet) => {
      this.displayName = value.userDisplayName;
      if (this.bets.scores.length === value.scores.length) {
        this.bets = value;
        this.updateLookup();
      }

    });
  }

  started(): boolean {
    if (this.game) {
      return this.game.matches.find(match => {
        return new Date(Date.parse(match.date)) <= (new Date());
      }) !== undefined;
    } else {
      return false;
    }
  }

}
