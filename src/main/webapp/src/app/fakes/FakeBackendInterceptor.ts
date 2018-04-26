import {Injectable} from '@angular/core';
import {HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/operator/materialize';
import 'rxjs/add/operator/dematerialize';
import {AuthenticationFilter} from './AuthenticationFilter';
import {UsersFilter} from './UsersFilter';
import {BetsFilter} from './BetsFilter';
import {GamesFilter} from './GamesFilter';
import {GroupsFilter} from './GroupsFilter';

@Injectable()
export class FakeBackendInterceptor implements HttpInterceptor {

  private filters = [new AuthenticationFilter(), new BetsFilter(), new UsersFilter(), new GamesFilter(), new GroupsFilter()];

  constructor() {

  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // wrap in delayed observable to simulate server api call
    return Observable.of(null).mergeMap(() =>
      this.filterRequest(request, next)
    )
    // call materialize and dematerialize to ensure delay even if an error is thrown (https://github.com/Reactive-Extensions/RxJS/issues/648)
      .materialize()
      .delay(500)
      .dematerialize();
  }


  private filterRequest(request: HttpRequest<any>, next: HttpHandler) {
    console.log('Filtering request');
    for (const filter of this.filters) {
      const ans = filter.filter(request);
      if (ans !== undefined) {
        console.log('Mocked response');
        return ans;
      }
    }
    // pass through any requests not handled above
    return next.handle(request);
  }
}

export let fakeBackendProvider = {
  // use fake backend in place of Http service for backend-less development
  provide: HTTP_INTERCEPTORS,
  useClass: FakeBackendInterceptor,
  multi: true
};
