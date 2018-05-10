import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {GroupsService} from '../groups.service';
import {Group} from '../group.model';
import {environment} from '../../environments/environment';
import {AuthenticationService} from '../authentication.service';
import {UserService} from '../user.service';


@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css']
})
export class GroupComponent implements OnInit, OnDestroy {

  private sub: Subscription;
  group: Group;
  link: any;
  userDisplayName: String;
  groupId: number;
  memberGroups: Group[];

  constructor(private route: ActivatedRoute,
              private groupService: GroupsService,
              private authService: AuthenticationService,
              private userService: UserService) {
  }

  ngOnInit() {
    this.groupId = this.route.snapshot.params.group;
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }

  onSubmit() {
    const user = this.authService.currentUser();
    if (!user.displayNames) {
      user.displayNames = {};
    }
    user.displayNames[this.groupId] = this.userDisplayName;
  }

  private generateLink() {
    return environment.host + '/join/' + this.group.key;
  }


  private loadGroup(groupId: number) {
    this.groupService.get(groupId).subscribe(ans => {
      this.group = ans;
      this.link = this.generateLink();
    });
    this.userDisplayName = this.authService.currentUser().username;
    if (this.authService.currentUser().displayNames && this.authService.currentUser().displayNames[groupId]) {
      this.userDisplayName = this.authService.currentUser().displayNames[groupId];
    }
  }
}
