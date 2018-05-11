import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {UserService} from '../user.service';
import {AlertService} from '../alert.service';
import {User} from '../user';
import {HttpErrorResponse} from '@angular/common/http';



@Component({
  moduleId: module.id.toString(),
  templateUrl: 'register.component.html'
})

export class RegisterComponent {
  model: User = new User();
  loading = false;

  constructor(
    private router: Router,
    private userService: UserService,
    private alertService: AlertService) { }

  register() {
    this.loading = true;
    this.userService.create(this.model)
      .subscribe(
        data => {
          this.alertService.success('Registration successful', true);
          this.router.navigate(['/login']);
        },
        (error: HttpErrorResponse) => {

          this.alertService.error(error.message);
          this.loading = false;
        });
  }
}
