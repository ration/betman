import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../authentication.service';
import {AlertService} from '../alert.service';
import {HttpErrorResponse} from '@angular/common/http';


@Component({
  moduleId: module.id.toString(),
  templateUrl: 'login.component.html'
})

export class LoginComponent implements OnInit {
  model: any = {};
  loading = false;
  returnUrl: string;
  key: string = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private alertService: AlertService) {
  }

  ngOnInit() {
    // reset login status
    this.authenticationService.logout();

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

    this.key = this.route.snapshot.params.key;
  }

  login() {
    this.loading = true;
    this.authenticationService.login(this.model.username, this.model.password)
      .subscribe(
        data => {
          if (this.key) {
            this.router.navigate(['/join', this.key]);
          } else {
            this.router.navigate([this.returnUrl]);
          }
        },
        (error: HttpErrorResponse) => {

          this.alertService.error(error.error['message']);
          this.loading = false;
        });
  }
}
