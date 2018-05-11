import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from './user';
import {AuthenticationService} from './authentication.service';
import {environment} from '../environments/environment';


@Injectable()
export class UserService {
  private registerUrl = environment.host + '/api/users/register';
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


}
