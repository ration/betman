import {Component, OnInit} from '@angular/core';
import {Bet} from '../bet.model';
import {GamesService} from '../games.service';
import {Subject} from 'rxjs';


import {AuthenticationService} from '../authentication.service';
import {Game, Match} from '../game.model';
import {AlertService} from '../alert.service';
import {debounceTime, flatMap} from 'rxjs/operators';
import {GroupsService} from '../groups.service';
import {Group} from '../group.model';

@Component({
  selector: 'app-betting',
  templateUrl: './betting.component.html',
  styleUrls: ['./betting.component.css']
})
export class BettingComponent implements OnInit {

  game: Game = {id: -1, name: '', matches: [], description: ''};
  bets: Bet = {scores: []};
  private saveSubject: Subject<Bet> = new Subject<Bet>();
  private user = '';

  constructor(private gamesService: GamesService,
              private authService: AuthenticationService,
              private alertService: AlertService,
              private groupService: GroupsService) {
    const key = this.groupService.getActive();
    if (key) {
      this.bets.groupKey = key;
      this.groupService.get(key).pipe(
        flatMap((group: Group) => this.gamesService.all(group.game))
      ).subscribe((data: Game) => {
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
      for (const bet of Object.values(this.bets)) {
        bet.home = BettingComponent.getRandomGoals(1, 5);
        bet.away = BettingComponent.getRandomGoals(1, 5);
      }
    }
  }

  onChange(change) {
    this.saveSubject.next(this.bets);
  }

  onSubmit(value: Bet) {
    this.gamesService.saveBet(this.bets.groupKey, value).subscribe(res => {
      this.alertService.success('Saved', false, 2000);
    });
  }

  canBet(match: Match): boolean {
    const now = new Date();
    return new Date(Date.parse(match.date)) >= now;
  }

  private getBettingData() {

    for (const game of this.game.matches) {
      this.bets.scores.push({id: game.id, home: 0, away: 0});
    }
    this.gamesService.bets(this.bets.groupKey).subscribe((value: Bet) => {
      if (this.bets.scores.length === value.scores.length) {
        this.bets.scores = value.scores;
      }
    });
  }

  ngOnInit() {
  }

}
