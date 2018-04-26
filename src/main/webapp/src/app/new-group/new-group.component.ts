import {Component, OnInit} from '@angular/core';
import {GroupsService} from '../groups.service';
import {Group} from '../group.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-new-group',
  templateUrl: './new-group.component.html',
  styleUrls: ['./new-group.component.css']
})
export class NewGroupComponent implements OnInit {
  model: Group = new Group();

  constructor(private groupService: GroupsService,
              private router: Router) {
  }

  ngOnInit() {
  }

  onSubmit() {
    this.groupService.newGroup(this.model).subscribe((saved: Group) => {
      this.router.navigate(['/group', saved.id]);
    });
  }
}
