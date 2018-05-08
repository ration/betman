import {Component, OnInit} from '@angular/core';
import {GroupsService} from '../groups.service';
import {Group} from '../group.model';
import {Router} from '@angular/router';
import {FormControl, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-new-group',
  templateUrl: './new-group.component.html',
  styleUrls: ['./new-group.component.css']
})
export class NewGroupComponent implements OnInit {
  model: Group = new Group();
  newGroup: FormGroup;

  constructor(private groupService: GroupsService,
              private router: Router) {
  }

  ngOnInit() {
    this.newGroup = new FormGroup({
      name: new FormControl(''),
      description: new FormControl('')
    });
  }

  onSubmit() {
    this.groupService.newGroup(this.model).subscribe((saved: Group) => {
      this.router.navigate(['/group', saved.key]);
    });
  }
}
