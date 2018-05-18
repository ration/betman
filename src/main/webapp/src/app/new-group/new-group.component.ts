import {Component, OnInit} from '@angular/core';
import {GroupsService} from '../groups.service';
import {Group} from '../group.model';
import {Router} from '@angular/router';
import {AlertService} from '../alert.service';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-new-group',
  templateUrl: './new-group.component.html',
  styleUrls: ['./new-group.component.css']
})
export class NewGroupComponent implements OnInit {
  model: Group = new Group();

  constructor(private groupService: GroupsService,
              private router: Router,
              private alertService: AlertService) {
    this.model.game = 'Fifa2018';
    this.model.name = '';
    this.model.description = '';
  }

  ngOnInit() {

  }

  onSubmit() {
    this.groupService.newGroup(this.model).subscribe((saved: Group) => {
      this.groupService.setActive(saved.key);
      this.router.navigate(['/group', saved.key]);
    }, (error: HttpErrorResponse) => this.alertService.error(error.message));
  }
}
