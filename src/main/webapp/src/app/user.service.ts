import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {User} from './user.model';
import {AuthenticationService} from './authentication.service';
import {environment} from '../environments/environment';
import {Observable} from 'rxjs';


@Injectable()
export class UserService {
  private registerUrl = environment.host + '/api/users/register';
  private updateUrl = environment.host + '/api/users/update';

  constructor(private http: HttpClient, private authService: AuthenticationService) {
  }

  getAll() {
    return this.http.get<User[]>('/api/users');
  }

  getById(id: number) {
    return this.http.get('/api/users/' + id);
  }

  create(user: User) {
    return this.http.post(this.registerUrl, user);
  }


  update(user: User): Observable<HttpResponse<any>> {
    return this.http.post<any>(this.updateUrl, user, {observe: 'response'});
  }

}
