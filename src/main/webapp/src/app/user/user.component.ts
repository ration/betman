import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../authentication.service';
import {User} from '../user.model';
import {UserService} from '../user.service';
import {AlertService} from '../alert.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  user: User = {name: '', password: '', admin: false};
  currentUser: User = null;


  constructor(private authService: AuthenticationService,
              private userService: UserService,
              private alertService: AlertService) {
  }

  ngOnInit() {
    this.currentUser = this.authService.currentUser();
    this.user = this.currentUser;
  }

  logout() {
    this.authService.logout();
  }

  onSubmit() {
    this.userService.update(this.user).subscribe(
      response => this.alertService.success('User updated'),
      error => this.alertService.error(error.error['message'])
    );
  }
}
