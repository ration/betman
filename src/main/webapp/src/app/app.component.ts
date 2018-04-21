import {Component} from '@angular/core';
import {GamesService} from './games.service';
import {Bet} from 'app/bet.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Betman';
  games = null;
  bets = {};


  constructor(private gamesService: GamesService) {
    this.gamesService.all().subscribe(data => {
      this.games = data;
      this.getBettingData();
    });
  }

  private getBettingData() {
    for (const game of this.games) {
      this.bets[game.id] = new Bet(game.id, 1, 2);
    }
  }

  onChange(change) {
    console.log(JSON.stringify(this.bets));
  }

  onSubmit() {

  }
}
