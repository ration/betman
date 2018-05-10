import {ApiFilter} from './ApiFilter';
import {Observable, of} from 'rxjs';
import {HttpRequest, HttpResponse} from '@angular/common/http';
import {bets} from './bets';


export class BetsFilter implements ApiFilter {
  filter(request: HttpRequest<any>): Observable<HttpResponse<any>> {
    if (request.url.match(/\/api\/addBets/) && request.method === 'POST') {
      return of(new HttpResponse({status: 200, body: {}}));
    } else if (request.url.match(/\/api\/bets/) && request.method === 'GET') {
      return of(new HttpResponse({status: 200, body: bets}));
    }
    return undefined;
  }


}
