import {Component, OnInit} from '@angular/core';
import {Bet} from '../bet.model';
import {GamesService} from '../games.service';
import {Subject} from 'rxjs';


import {AuthenticationService} from '../authentication.service';
import {Game} from '../game.model';
import {AlertService} from '../alert.service';
import {debounceTime, distinctUntilChanged, flatMap, map} from 'rxjs/operators';
import {GroupsService} from '../groups.service';
import {Group} from '../group.model';

@Component({
  selector: 'app-betting',
  templateUrl: './betting.component.html',
  styleUrls: ['./betting.component.css']
})
export class BettingComponent implements OnInit {

  game: Game = null;
  bets: { [id: number]: Bet } = {};
  private saveSubject: Subject<Bet[]> = new Subject<Bet[]>();
  private user = '';
  private game = '';

  constructor(private gamesService: GamesService,
              private authService: AuthenticationService,
              private alertService: AlertService,
              private groupService: GroupsService) {
    const key = this.groupService.getActive();
    if (key) {
      this.groupService.get(key).pipe(
        flatMap((group: Group) => this.gamesService.all(group.game))
      ).subscribe((data: Game) => {
        this.game = data;
        this.getBettingData();
      });

    }

    this.saveSubject.pipe(debounceTime(2000), distinctUntilChanged()).subscribe(value => {
      this.onSubmit(value);
    });
    if (this.authService.currentUser() != null) {
      this.user = this.authService.currentUser().name;
    }
  }

  private getBettingData() {

    for (const game of this.game.matches) {
      this.bets[game.id] = new Bet(game.id, 0, 0);
    }
    this.gamesService.bets(this.game, this.user).subscribe((value: Bet[]) => {
      for (const game of value) {
        this.bets[game.id] = game;
      }
    });
  }

  guess() {
    if (confirm('Are you sure? This will overwrite all of your choices?')) {
      for (const bet of Object.values(this.bets)) {
        bet.home = this.getRandomGoals(1, 5);
        bet.away = this.getRandomGoals(1, 5);
      }
    }
  }

  private getRandomGoals(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }

  onChange(change) {
    this.saveSubject.next(Object.values(this.bets));
  }

  onSubmit(value: Bet[]) {
    this.gamesService.saveBet(this.game, this.user, value).subscribe(res => {
      this.alertService.success('Saved', false, 2000);
    });
  }

  ngOnInit() {
  }

}
