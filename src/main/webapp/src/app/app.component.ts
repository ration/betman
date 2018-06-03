import {AfterViewChecked, Component, OnInit} from '@angular/core';
import {AuthenticationService} from './authentication.service';
import {Observable} from 'rxjs';
import {GroupsService} from './groups.service';
import {filter, map} from 'rxjs/operators';
import {of} from 'rxjs/internal/observable/of';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewChecked {
  title = 'Betman';
  active: Observable<string> = of('Group');
  group: string = null;
  isLoggedIn: Observable<boolean>;

  constructor(private authService: AuthenticationService, private groupService: GroupsService) {

  }


  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();

    this.active = this.groupService.active().pipe(filter(it => it != null), map(it => it.name));
    this.active.subscribe(active => this.group = active);
  }

  ngAfterViewChecked() {
  }


  logout() {
    this.authService.logout();
  }
}
