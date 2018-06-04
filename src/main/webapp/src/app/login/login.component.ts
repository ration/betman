import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../authentication.service';
import {AlertService} from '../alert.service';
import {HttpErrorResponse} from '@angular/common/http';
import {Group} from '../group.model';
import {GroupsService} from '../groups.service';


@Component({
  moduleId: module.id.toString(),
  templateUrl: 'login.component.html'
})

export class LoginComponent implements OnInit {
  model: any = {};
  loading = false;
  returnUrl: string;
  key: string = null;
  group: Group = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private alertService: AlertService,
    private groupService: GroupsService) {
  }

  ngOnInit() {
    // reset login status
    this.authenticationService.logout();
    this.returnUrl = '/';


    this.key = this.route.snapshot.params.key;
    if (this.key) {
      this.groupService.get(this.key).subscribe((value: Group) =>
        this.group = value);
    }
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
