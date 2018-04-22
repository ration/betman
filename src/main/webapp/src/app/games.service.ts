import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Bet} from './bet.model';

@Injectable()
export class GamesService {
  private host = 'http://localhost:8080';
  public apiAllUrl = this.host + '/api/all';
  public saveBetUrl = this.host + '/api/addBets';
  public betsUrl = this.host + '/api/bets';

  constructor(private http: HttpClient) {
  }

  all() {
    return this.http.get(this.apiAllUrl);
  }

  saveBet(game: number, user: number, bets: Bet[]) {
    const params = new HttpParams().set('game', game.toString()).set('user', user.toString());
    return this.http.post(this.saveBetUrl, bets, {params});
  }

  bets(game: number, user: number) {
    const params = new HttpParams().set('game', game.toString()).set('user', user.toString());
    return this.http.get(this.betsUrl, {params});
  }
}
