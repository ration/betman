import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {GroupsService} from '../groups.service';
import {map} from 'rxjs/operators';
import {Group} from '../group.model';
import {AuthenticationService} from '../authentication.service';

@Component({
  selector: 'app-join',
  templateUrl: './join.component.html',
  styleUrls: ['./join.component.css']
})
export class JoinComponent implements OnInit {
  key: string = null;
  group = null;
  name: string = null;

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
    } else if (!this.authService.currentUser()) {
      this.router.navigate(['/login', this.key]);
    } else if (this.key) {
      this.groupService.get(this.key).subscribe(v => this.group = v);
    }
    this.name = this.authService.currentUser().name
  }

  join() {
    if (this.key && this.name) {
      this.groupService.join(this.key, this.name).subscribe(v => {
        this.router.navigate(['/group', v.key]);
      });
    }
  }


}
