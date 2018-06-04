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

  public guessUrl = environment.host + '/api/bets/guess';


  constructor(private http: HttpClient) {
  }

  all(game: string): Observable<Game> {
    return this.http.get<Game>(this.apiAllUrl + '/' + game);
  }

  saveBet(group: string, bets: Bet) {
    const params = new HttpParams().set('group', group);
    return this.http.post(this.saveBetUrl, bets, {params});
  }

  bets(game: string): Observable<Bet> {
    return this.http.get<Bet>(this.betsUrl + '/' + game);
  }

  betsForUser(game: string, user: string): Observable<Bet> {
    return this.http.get<Bet>(this.betsUrl + '/' + game + '/' + user);
  }

  guess(group: string, bets: Bet): Observable<Bet> {
    return this.http.post<Bet>(this.guessUrl + '/' + group, bets);
  }

}
