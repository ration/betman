import {Component, OnInit} from '@angular/core';
import {User} from '../user';
import {UserService} from '../user.service';
import {Group} from '../group.model';
import {GroupsService} from '../groups.service';
import {group} from '@angular/animations';


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
     this.groupService.all().subscribe( (groups: Group[]) => this.groups = groups );
  }

  groupSelect(selection) {
    console.log("Changing to " + selection);
    this.active = selection;
    this.groupService.setActive(selection);
  }


}
