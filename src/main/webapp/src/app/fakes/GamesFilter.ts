import {ApiFilter} from './ApiFilter';
import {allgames} from './allgames';
import {Observable, of} from 'rxjs';
import {HttpRequest, HttpResponse} from '@angular/common/http';


export class GamesFilter implements ApiFilter {
  filter(request: HttpRequest<any>): Observable<HttpResponse<any>> {
    if (request.url.match(/\/api\/all/) && request.method === 'GET') {
      return of(new HttpResponse({status: 200, body: allgames}));
    }
    return undefined;
  }
}

