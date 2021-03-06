import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {GroupsService} from '../groups.service';
import {Group} from '../group.model';
import {AuthenticationService} from '../authentication.service';
import {AlertService} from '../alert.service';

@Component({
  selector: 'app-join',
  templateUrl: './join.component.html',
  styleUrls: ['./join.component.css']
})
export class JoinComponent implements OnInit {
  key: string = null;
  group: Group = null;
  name: string = null;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private groupService: GroupsService,
              private authService: AuthenticationService,
              private alertService: AlertService) {
  }

  ngOnInit() {
    this.key = this.route.snapshot.params.key;
    if (this.key && this.authService.currentUser()) {
      this.name = this.authService.currentUser().name;

      this.groupService.get(this.key).subscribe((value: Group) => {
        this.group = value;
        if (this.group.userDisplayName) {
          this.groupService.setActive(this.group.key);
          this.router.navigate(['/group', this.group.key]);
        }
      });
    } else if (!this.authService.currentUser()) {
      this.router.navigate(['/login', this.key]);
    } else if (this.key) {
      this.groupService.get(this.key).subscribe(v => this.group = v);
    } else if (this.authService.currentUser()) {
      this.name = this.authService.currentUser().name;
    }
  }


  join() {
    if (this.key && this.name) {
      this.groupService.join(this.key, this.name).subscribe(v => {
        this.groupService.setActive(v.key);
        this.router.navigate(['/group', v.key]);
      }, error => this.alertService.error(error.error['message']));
    }
  }


}
