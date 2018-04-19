import {Component} from '@angular/core';
import {GamesService} from "./games.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
  games = null;


  constructor(private gamesService: GamesService) {
    this.gamesService.all().subscribe( data => {
      this.games = data;
    })
  }


}
