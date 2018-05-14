import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {GroupsService} from '../groups.service';
import {Group} from '../group.model';
import {environment} from '../../environments/environment';
import {AuthenticationService} from '../authentication.service';
import {UserService} from '../user.service';
import {AlertService} from '../alert.service';
import {HttpErrorResponse} from '@angular/common/http';


@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css']
})
export class GroupComponent implements OnInit, OnDestroy {

  private sub: Subscription;
  group: Group;
  link: any;
  userDisplayName: string;
  groupId: string;
  memberGroups: Group[];

  constructor(private route: ActivatedRoute,
              private groupService: GroupsService,
              private authService: AuthenticationService,
              private userService: UserService,
              private alertService: AlertService) {
  }

  ngOnInit() {
    this.groupId = this.route.snapshot.params.group;
    if (this.groupId) {
      this.loadGroup(this.groupId);
    }
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }

  onSubmit() {
    if (this.group.key) {
      this.groupService.updateDisplayName(this.group.key, this.userDisplayName).subscribe((result) => {
        this.alertService.success('Saved');
      }, (error: HttpErrorResponse) => this.alertService.error(error.message));
    }
  }

  private generateLink() {
    return environment.host + '/join/' + this.group.key;
  }


  private loadGroup(groupId: string) {
    this.groupService.get(groupId).subscribe(ans => {
      this.group = ans;
      this.link = this.generateLink();
      if (this.group.userDisplayName) {
        this.userDisplayName = this.group.userDisplayName;
      } else {
        this.userDisplayName = this.authService.currentUser().name;
      }
    });
  }
}
