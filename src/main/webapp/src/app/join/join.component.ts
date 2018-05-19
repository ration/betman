import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {GroupsService} from '../groups.service';
import {filter, find, map} from 'rxjs/operators';
import {Group} from '../group.model';
import {AuthenticationService} from '../authentication.service';

@Component({
  selector: 'app-join',
  templateUrl: './join.component.html',
  styleUrls: ['./join.component.css']
})
export class JoinComponent implements OnInit {
  key = null;
  group = null;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private groupService: GroupsService,
              private authService: AuthenticationService) {
  }

  ngOnInit() {
    this.key = this.route.snapshot.params.key;
    if (this.key && this.authService.currentUser()) {
      const isMember = this.groupService.all().pipe(map(value => value.find(v => v.key === this.key)));
      isMember.subscribe((value: Group) => {
        this.group = value;

        if (!this.group) {
          this.groupService.get(this.key).subscribe(v => this.group = v);
        } else {
          this.router.navigate(['/group', this.group.key]);
        }
      });
    } else if (this.key) {
      this.groupService.get(this.key).subscribe(v => this.group = v);
    }
  }

}
