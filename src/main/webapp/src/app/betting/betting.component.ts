import {Component, OnInit} from '@angular/core';
import {Bet} from '../bet.model';
import {GamesService} from '../games.service';
import {Subject} from 'rxjs/Subject';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import {AuthenticationService} from '../authentication.service';
import {Game} from '../game.model';
import {AlertService} from '../alert.service';
import {getRandomString} from 'selenium-webdriver/safari';

@Component({
  selector: 'app-betting',
  templateUrl: './betting.component.html',
  styleUrls: ['./betting.component.css']
})
export class BettingComponent implements OnInit {

  games: Game[] = [];
  bets: { [id: number]: Bet } = {};
  private saveSubject: Subject<Bet[]> = new Subject<Bet[]>();
  private user = 1;
  private game = 1;

  constructor(private gamesService: GamesService, private authService: AuthenticationService,
              private alertService: AlertService) {
    this.gamesService.all().subscribe((data: Game[]) => {
      this.games = data;
      this.getBettingData();
    });

    this.saveSubject.debounceTime(2000).distinctUntilChanged().subscribe(value => {
      this.onSubmit(value);
    });
    this.user = this.authService.currentUser().id;
  }

  private getBettingData() {
    for (const game of this.games) {
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
