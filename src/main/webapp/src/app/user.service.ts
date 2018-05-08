import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from './user';
import {AuthenticationService} from './authentication.service';


@Injectable()
export class UserService {
  constructor(private http: HttpClient, private authService: AuthenticationService) {
  }

  getAll() {
    return this.http.get<User[]>('/api/users');
  }

  getById(id: number) {
    return this.http.get('/api/users/' + id);
  }

  create(user: User) {
    return this.http.post('/api/users/register', user);
  }


}
