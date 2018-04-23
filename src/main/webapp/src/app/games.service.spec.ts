import {inject, TestBed} from '@angular/core/testing';

import {GamesService} from './games.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Bet} from './bet.model';

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

      const mockGames = [{
        'id': 1,
        'home': {'name': 'Russia'},
        'away': {'name': 'Saudi Arabia'},
        'date': '2018-06-14T15:00:00.000+0000'
      }, {'id': 2, 'home': {'name': 'Egypt'}, 'away': {'name': 'Uruguay'}, 'date': '2018-06-15T12:00:00.000+0000'}];

      gamesService.all().subscribe((res: Object) => {
        expect(res).toEqual(mockGames);
      });
      const req = httpMock.expectOne(gamesService.apiAllUrl);
      expect(req.request.responseType).toEqual('json');
      req.flush(mockGames);
      httpMock.verify();
    }));

  it('gets bets.ts', inject(
    [HttpTestingController, GamesService],
    (httpMock: HttpTestingController, gamesService: GamesService) => {
      const bets = [new Bet(1, 2, 3)];
      gamesService.bets(1, 2).subscribe((res: Object) => {
        expect(res).toBe(bets);
      });
      const req = httpMock.expectOne(gamesService.betsUrl + '?game=1&user=2');
      req.flush(bets);
      httpMock.verify();
    }));

  it('stores bets.ts', inject(
    [HttpTestingController, GamesService],
    (httpMock: HttpTestingController, gamesService: GamesService) => {
      const bets = [new Bet(1, 2, 3)];
      gamesService.saveBet(1, 2, bets).subscribe((res: Object) => {
        expect(res).toBe(null);
      });
      const req = httpMock.expectOne(gamesService.saveBetUrl + '?game=1&user=2');
      req.flush(null);
      httpMock.verify();
    }));
});
