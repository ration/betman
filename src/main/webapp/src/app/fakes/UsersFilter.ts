import {ApiFilter} from './ApiFilter';
import {HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {User} from '../user';

export class UsersFilter implements ApiFilter {

  filter(request: HttpRequest<any>): Observable<HttpResponse<any>> {
    // array in local storage for registered users
    const users: User[] = JSON.parse(localStorage.getItem('users')) || [];

    if (request.url.match(/\/api\/users\/\d+$/) && request.method === 'GET') {
      return this.getUsers(request, users);
    }

    if (request.url.match('/api/users') && (request.method === 'POST')) {
      return this.add(request, users);
    }

    if (request.url.match('/api/users') && request.method === 'PUT') {
      return this.update(request, users);
    }

    return undefined;
  }

  private getUsers(request: HttpRequest<any>, users: User[]): Observable<HttpResponse<any>> {
    // check for fake auth token in header and return user if valid, this security is implemented server side in a real application
    if (request.headers.get('Authorization') === 'Bearer fake-jwt-token') {
      // find user by id in users array
      /*
      TODO something else
      const urlParts = request.url.split('/');
      const id = parseInt(urlParts[urlParts.length - 1], 10);
      const matchedUsers = users.filter((it: User) => {
        return it.username === id;
      });
      const user = matchedUsers.length ? matchedUsers[0] : null;
*/
      const user: User = {username: 'myname'};
      return Observable.of(new HttpResponse({status: 200, body: user}));
    } else {
      // return 401 not authorised if token is null or invalid
      return Observable.throw('Unauthorised');
    }

  }

  private update(request: HttpRequest<any>, users: User[]): Observable<HttpResponse<any>> {
    localStorage.setItem('users', JSON.stringify(users));
    return Observable.of(new HttpResponse({status: 200}));
  }

  private add(request: HttpRequest<any>, users: User[]): Observable<HttpResponse<any>> {
    // get new user object from post body
    const newUser: User = request.body;

    // validation
    const duplicateUser = users.filter(user => {
      return user.username === newUser.username;
    }).length;
    if (duplicateUser) {
      return Observable.throw('Username "' + newUser.username + '" is already taken');
    }

    // save new user
    // newUser = users.length + 1;
    // TODO fix this
    users.push(newUser);
    localStorage.setItem('users', JSON.stringify(users));

    // respond 200 OK
    return Observable.of(new HttpResponse({status: 200}));
  }


}
