import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Bet} from './bet.model';
import {environment} from '../environments/environment';
import {Observable} from 'rxjs/internal/Observable';
import {Game} from './game.model';

@Injectable()
export class GamesService {
  public apiAllUrl = environment.host + '/api/games';
  public saveBetUrl = environment.host + '/api/bets/update';
  public betsUrl = environment.host + '/api/bets';

  constructor(private http: HttpClient) {
  }

  all(game: string): Observable<Game> {
    return this.http.get(this.apiAllUrl + '/' + game);
  }

  saveBet(game: string, user: string, bets: Bet[]) {
    const params = new HttpParams().set('game', game).set('user', user);
    return this.http.post(this.saveBetUrl, bets, {params});
  }

  bets(game: string, user: string) {
    const params = new HttpParams().set('game', game).set('user', user);
    return this.http.get(this.betsUrl, {params});
  }
}
