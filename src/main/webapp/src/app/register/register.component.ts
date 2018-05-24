///<reference path="../../../node_modules/@angular/core/src/metadata/lifecycle_hooks.d.ts"/>
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../user.service';
import {AlertService} from '../alert.service';
import {User} from '../user';
import {HttpErrorResponse} from '@angular/common/http';
import {AuthenticationService} from '../authentication.service';


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
    private alertService: AlertService,
    private authenticationService: AuthenticationService
  ) {
  }

  register() {
    this.loading = true;
    this.userService.create(this.model)
      .subscribe(
        data => {
          this.alertService.success('Registration successful', true);
          // I don't understand why I can't do create().pipe(login).subscribe
          this.authenticationService.login(this.model.name, this.model.password)
            .subscribe(user => {
              if (this.key) {
                this.router.navigate(['/join', this.key]);
              } else {
                this.router.navigate(['/']);
              }
            });
        },
        (response: HttpErrorResponse) => {
          this.alertService.error(response.error['message']);
          this.loading = false;
        });
  }

  ngOnInit(): void {
    this.key = this.route.snapshot.params.key;
  }
}
