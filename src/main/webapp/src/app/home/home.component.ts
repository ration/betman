import {Component, OnInit} from '@angular/core';
import {User} from '../user.model';
import {UserService} from '../user.service';
import {Group} from '../group.model';
import {GroupsService} from '../groups.service';


@Component({
  moduleId: module.id.toString(),
  templateUrl: 'home.component.html'
})

export class HomeComponent implements OnInit {
  currentUser: User;
  groups: Group[] = [];
  active = null;

  constructor(private userService: UserService,
              private groupService: GroupsService) {
    if (localStorage.getItem('currentUser')) {
      this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    }
    this.active = groupService.getActive();
  }

  ngOnInit() {
    this.groupService.all().subscribe((groups: Group[]) => {
      this.groups = groups;

      if (!this.active && groups.length > 0) {
        this.groupService.setActive(groups[0].key);
      } else {
        this.groupService.setActive(this.active);
      }
    });
  }

  groupSelect(selection) {
    this.active = selection;
    this.groupService.setActive(selection);
  }
}
