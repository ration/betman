import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import 'rxjs/add/operator/map';
import {User} from './user';
import {Observable} from 'rxjs/Observable';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Router} from '@angular/router';

@Injectable()
export class AuthenticationService {
  private loggedIn = new BehaviorSubject<boolean>(false);


  constructor(private http: HttpClient, private router: Router) {
    this.loggedIn.next(localStorage.getItem('currentUser') != null);
  }

  login(username: string, password: string) {
    return this.http.post<any>('/api/authenticate', {username: username, password: password})
      .map(user => {
        // login successful if there's a jwt token in the response
        if (user && user.token) {
          // store user details and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem('currentUser', JSON.stringify(user));
          this.loggedIn.next(true);
        }

        return user;
      });
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  currentUser(): User {
    return JSON.parse(localStorage.getItem('currentUser'));
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
