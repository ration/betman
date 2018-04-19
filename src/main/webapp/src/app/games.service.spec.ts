import {inject, TestBed} from '@angular/core/testing';

import {GamesService} from './games.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";

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
        "id": 1,
        "home": {"name": "Russia"},
        "away": {"name": "Saudi Arabia"},
        "date": "2018-06-14T15:00:00.000+0000"
      }, {"id": 2, "home": {"name": "Egypt"}, "away": {"name": "Uruguay"}, "date": "2018-06-15T12:00:00.000+0000"}];

      gamesService.all().subscribe(res => {
        expect(res).toEqual(mockGames);
      });
      const req = httpMock.expectOne("api/all");
      expect(req.request.responseType).toEqual('json');
      req.flush(mockGames);
      httpMock.verify();
    }));
});
