import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {User} from './user';
import {Router} from '@angular/router';
import {BehaviorSubject, Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {environment} from '../environments/environment';


@Injectable()
export class AuthenticationService {
  private loginUrl = environment.host + '/api/users/login';
  private loggedIn = new BehaviorSubject<boolean>(false);


  constructor(private http: HttpClient, private router: Router) {
    this.loggedIn.next(localStorage.getItem('currentUser') != null);
  }

  login(username: string, password: string) {
    const sendUser: User = {
      name: username,
      password: password
    };
    return this.http.post<any>(this.loginUrl, sendUser)
      .pipe(map((user: User) => {
        // login successful if there's a jwt token in the response
        if (user.token) {
          // store user details and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem('currentUser', JSON.stringify(user));
          this.loggedIn.next(true);
        }

        return user;
      }));
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  currentUser(): User {
    if (localStorage.getItem('currentUser')) {
      return JSON.parse(localStorage.getItem('currentUser'));
    }
    return null;
  }

  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('currentUser');
    this.loggedIn.next(false);
    this.router.navigate(['/login']);

  }

  updateCurrentUser(user: User) {
    localStorage.setItem('currentUser', JSON.stringify(user));
  }
}
