import {Injectable} from '@angular/core';
import {HttpRequest, HttpResponse, HttpHandler, HttpEvent, HttpInterceptor, HTTP_INTERCEPTORS} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/operator/materialize';
import 'rxjs/add/operator/dematerialize';
import {User} from '../user';
import {allgames} from './allgames';
import {bets} from './bets';

@Injectable()
export class FakeBackendInterceptor implements HttpInterceptor {

  constructor() {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // array in local storage for registered users
    const users: User[] = JSON.parse(localStorage.getItem('users')) || [];

    // wrap in delayed observable to simulate server api call
    return Observable.of(null).mergeMap(() =>
      this.filterRequest(request, next, users)
    )
    // call materialize and dematerialize to ensure delay even if an error is thrown (https://github.com/Reactive-Extensions/RxJS/issues/648)
      .materialize()
      .delay(500)
      .dematerialize();
  }

  private authenticate(request: HttpRequest<any>, users: User[]): Observable<HttpResponse<any>> {
    // find if any user matches login credentials
    const filteredUsers = users.filter(user => {
      return user.username === request.body.username && user.password === request.body.password;
    });

    if (filteredUsers.length) {
      // if login details are valid return 200 OK with user details and fake jwt token
      const user = filteredUsers[0];
      const body = {
        id: user.id,
        username: user.username,
        displayName: user.displayName,
        token: 'fake-jwt-token'
      };

      return Observable.of(new HttpResponse({status: 200, body: body}));
    } else {
      // else return 400 bad request
      return Observable.throw('Username or password is incorrect');
    }
  }


  private getUsers(request: HttpRequest<any>, users: User[]): Observable<HttpResponse<any>> {
    // check for fake auth token in header and return user if valid, this security is implemented server side in a real application
    if (request.headers.get('Authorization') === 'Bearer fake-jwt-token') {
      // find user by id in users array
      const urlParts = request.url.split('/');
      const id = parseInt(urlParts[urlParts.length - 1], 10);
      const matchedUsers = users.filter((it: User) => {
        return it.id === id;
      });
      const user = matchedUsers.length ? matchedUsers[0] : null;

      return Observable.of(new HttpResponse({status: 200, body: user}));
    } else {
      // return 401 not authorised if token is null or invalid
      return Observable.throw('Unauthorised');
    }

  }


  private addUser(request: HttpRequest<any>, users: User[]): Observable<HttpResponse<any>> {
    // get new user object from post body
    const newUser = request.body;

    // validation
    const duplicateUser = users.filter(user => {
      return user.username === newUser.username;
    }).length;
    if (duplicateUser) {
      return Observable.throw('Username "' + newUser.username + '" is already taken');
    }

    // save new user
    newUser.id = users.length + 1;
    users.push(newUser);
    localStorage.setItem('users', JSON.stringify(users));

    // respond 200 OK
    return Observable.of(new HttpResponse({status: 200}));
  }

  private delete(request: HttpRequest<any>, users: User[]): Observable<HttpResponse<any>> {
    if (request.headers.get('Authorization') === 'Bearer fake-jwt-token') {
      // find user by id in users array
      const urlParts = request.url.split('/');
      const id = parseInt(urlParts[urlParts.length - 1], 10);
      for (let i = 0; i < users.length; i++) {
        const user = users[i];
        if (user.id === id) {
          // delete user
          users.splice(i, 1);
          localStorage.setItem('users', JSON.stringify(users));
          break;
        }
      }

      // respond 200 OK
      return Observable.of(new HttpResponse({status: 200}));
    } else {
      // return 401 not authorised if token is null or invalid
      return Observable.throw('Unauthorised');
    }
  }


  private games(request: HttpRequest<any>): Observable<HttpResponse<any>> {
    return Observable.of(new HttpResponse({status: 200, body: allgames}));
  }

  private bets(request: HttpRequest<any>): Observable<HttpResponse<any>> {
    return Observable.of(new HttpResponse({status: 200, body: bets}));
  }

  private addBets(request: HttpRequest<any>): Observable<HttpResponse<any>> {
    return Observable.of(new HttpResponse({status: 200}));
  }

  private filterRequest(request: HttpRequest<any>, next: HttpHandler, users: User[]) {
    console.log("Filtering request");
    // authenticate
    if (request.url.endsWith('/api/authenticate') && request.method === 'POST') {
      return this.authenticate(request, users);
    }

    // get users
    if (request.url.endsWith('/api/users') && request.method === 'GET') {
      // check for fake auth token in header and return users if valid, this security is implemented server side in a real application
      if (request.headers.get('Authorization') === 'Bearer fake-jwt-token') {
        return Observable.of(new HttpResponse({status: 200, body: users}));
      } else {
        // return 401 not authorised if token is null or invalid
        return Observable.throw('Unauthorised');
      }
    }

    // get user by id
    if (request.url.match(/\/api\/users\/\d+$/) && request.method === 'GET') {
      return this.getUsers(request, users);
    }

    if (request.url.endsWith('/api/users') && request.method === 'POST') {
      return this.addUser(request, users);
    }

    if (request.url.match(/\/api\/users\/\d+$/) && request.method === 'DELETE') {
      return this.delete(request, users);
    }

    if (request.url.match(/\/api\/all/) && request.method === 'GET') {
      return this.games(request);
    }

    if (request.url.match(/\/api\/bets/) && request.method === 'GET') {
      return this.bets(request);
    }

    if (request.url.match(/\/api\/addBets/) && request.method === 'POST') {
      return this.addBets(request);
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
