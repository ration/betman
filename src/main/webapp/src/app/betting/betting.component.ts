import {Component, OnInit} from '@angular/core';
import {Bet} from '../bet.model';
import {GamesService} from '../games.service';
import {Subject} from 'rxjs/Subject';
import {Game} from '../game';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';

@Component({
  selector: 'app-betting',
  templateUrl: './betting.component.html',
  styleUrls: ['./betting.component.css']
})
export class BettingComponent implements OnInit {

  games = null;
  bets = {};
  private saveSubject: Subject<Bet[]> = new Subject<Bet[]>();
  private user = '1';
  private game = '1';

  constructor(private gamesService: GamesService) {
    this.gamesService.all().subscribe(data => {
      this.games = data;
      this.getBettingData();
    });

    this.saveSubject.debounceTime(2000).distinctUntilChanged().subscribe(value => {
      this.onSubmit(value);
    });

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

  onChange(change) {
    this.saveSubject.next(Object.values(this.bets));
  }

  onSubmit(value: Bet[]) {
    this.gamesService.saveBet(this.game, this.user, value).subscribe(res => {
      console.log('Saved: ' + res);
    });
  }

  ngOnInit() {
  }

}
