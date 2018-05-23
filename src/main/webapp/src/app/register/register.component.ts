///<reference path="../../../node_modules/@angular/core/src/metadata/lifecycle_hooks.d.ts"/>
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../user.service';
import {AlertService} from '../alert.service';
import {User} from '../user';
import {HttpErrorResponse} from '@angular/common/http';


@Component({
  moduleId: module.id.toString(),
  templateUrl: 'register.component.html'
})

export class RegisterComponent implements OnInit {
  model: User = new User();
  loading = false;
  key: string = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private alertService: AlertService) {
  }

  register() {
    this.loading = true;
    this.userService.create(this.model)
      .subscribe(
        data => {
          this.alertService.success('Registration successful', true);
          if (this.key) {
            this.router.navigate(['/login', this.key]);
          } else {
            this.router.navigate(['/login']);
          }
        },
        (error: HttpErrorResponse) => {

          this.alertService.error(error.message);
          this.loading = false;
        });
  }

  ngOnInit(): void {
    this.key = this.route.snapshot.params.key;
  }
}
