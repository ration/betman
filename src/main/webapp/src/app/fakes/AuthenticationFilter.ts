import {Observable, of, throwError as observableThrowError} from 'rxjs';
import {ApiFilter} from './ApiFilter';
import {HttpRequest, HttpResponse} from '@angular/common/http';
import {User} from '../user.model';

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
    const filteredUsers = users.filter((user: User) => {
      return user.name === request.body.name && user.password === request.body.password;
    });

    if (filteredUsers.length) {
      // if login details are valid return 200 OK with user details and fake jwt token
      const user = filteredUsers[0];
      const body = {
        name: user.name,
        token: 'fake-jwt-token',
        memberGroups: [1]
      };

      return of(new HttpResponse({status: 200, body: body}));
    } else {
      // else return 400 bad request
      return observableThrowError('Username or password is incorrect');
    }
  }
}
