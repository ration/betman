import {Component, OnInit} from '@angular/core';
import {GroupsService} from '../groups.service';

@Component({
  selector: 'app-new-group',
  templateUrl: './new-group.component.html',
  styleUrls: ['./new-group.component.css']
})
export class NewGroupComponent implements OnInit {

  private token: String;

  constructor(private groupService: GroupsService) {
    this.groupService.newInviteKey().subscribe(value => this.token = value);
  }

  ngOnInit() {
  }


}
