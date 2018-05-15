import {inject, TestBed} from '@angular/core/testing';

import {GamesService} from './games.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Bet} from './bet.model';
import {Game} from './game.model';

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

      const mockGames: Game[] = [{
        'id': 1,
        'description': 'qualif',
        'home': {'name': 'Russia', 'iso': 'ru'},
        'away': {'name': 'Saudi Arabia', 'iso': 'sa'},
        'date': '2018-06-14T15:00:00.000+0000'
      }, {
        'id': 2,
        'description': 'qualif',
        'home': {'name': 'Egypt', 'iso': 'eg'},
        'away': {'name': 'Uruguay', 'iso': 'ug'},
        'date': '2018-06-15T15:00:00.000+0000'
      }];


      gamesService.all('1').subscribe((res: Game[]) => {
        expect(res).toEqual(mockGames);
      });
      const req = httpMock.expectOne(gamesService.apiAllUrl + '/1');
      expect(req.request.responseType).toEqual('json');
      req.flush(mockGames);
      httpMock.verify();
    }));

  it('gets bets.ts', inject(
    [HttpTestingController, GamesService],
    (httpMock: HttpTestingController, gamesService: GamesService) => {
      const bets = [new Bet(1, 2, 3)];
      gamesService.bets('1', '2').subscribe((res: Object) => {
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
      gamesService.saveBet('1', '2', bets).subscribe((res: Object) => {
        expect(res).toBe(null);
      });
      const req = httpMock.expectOne(gamesService.saveBetUrl + '?game=1&user=2');
      req.flush(null);
      httpMock.verify();
    }));
});
