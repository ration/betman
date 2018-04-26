import {ApiFilter} from './ApiFilter';
import {HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {User} from '../user';

export class AuthenticationFilter implements ApiFilter {
  filter(request: HttpRequest<any>): Observable<HttpResponse<any>> {
    const users: User[] = JSON.parse(localStorage.getItem('users')) || [];

    if (request.url.endsWith('/api/authenticate') && request.method === 'POST') {
      return this.authenticate(request, users);
    }
    return undefined;
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
        token: 'fake-jwt-token',
        memberGroups: [1]
      };

      return Observable.of(new HttpResponse({status: 200, body: body}));
    } else {
      // else return 400 bad request
      return Observable.throw('Username or password is incorrect');
    }
  }
}
