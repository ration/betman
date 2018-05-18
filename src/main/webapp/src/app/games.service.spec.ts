import {inject, TestBed} from '@angular/core/testing';

import {GamesService} from './games.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Bet} from './bet.model';
import {Game, Match, Team} from './game.model';

describe('GamesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [GamesService]
    });
  });

  it('should be created', inject([GamesService], (service: GamesService) => {
    expect(service).toBeTruthy();
  }));

  it('gives games', inject(
    [HttpTestingController, GamesService],
    (httpMock: HttpTestingController, gamesService: GamesService) => {

      const germany: Team = {id: 1, iso: 'ge', name: 'Germany'};
      const england: Team = {id: 2, iso: 'gb', name: 'England'};

      const match1: Match = {'id': 1, home: germany, away: england, date: '2018-06-14T15:00:00.000+0000', description: 'preliminary'};

      const mockGame: Game = {
        name: 'Fifa2018',
        description: 'Fifa2018',
        id: 1,
        matches: [match1]
      };


      gamesService.all('1').subscribe((res: Game) => {
        expect(res).toEqual(mockGame);
      });
      const req = httpMock.expectOne(gamesService.apiAllUrl + '/1');
      expect(req.request.responseType).toEqual('json');
      req.flush(mockGame);
      httpMock.verify();
    }));

  it('gets bets', inject(
    [HttpTestingController, GamesService],
    (httpMock: HttpTestingController, gamesService: GamesService) => {
      const bets: Bet = {
        groupKey: 'key',
        id: 1,
        scores: [{id: 1, home: 1, away: 2}]
      };
      gamesService.bets('1').subscribe((res: Object) => {
        expect(res).toBe(bets);
      });
      const req = httpMock.expectOne(gamesService.betsUrl + '/1');
      req.flush(bets);
      httpMock.verify();
    }));

  it('stores bets', inject(
    [HttpTestingController, GamesService],
    (httpMock: HttpTestingController, gamesService: GamesService) => {
      const bets: Bet = {
        groupKey: 'key',
        id: 1,
        scores: [{id: 1, home: 1, away: 2}]
      };
      gamesService.saveBet('1', bets).subscribe((res: Object) => {
        expect(res).toBe(null);
      });
      const req = httpMock.expectOne(gamesService.saveBetUrl + '?group=1');
      req.flush(null);
      httpMock.verify();
    }));
});
