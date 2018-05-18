import {AfterViewChecked, Component, OnInit} from '@angular/core';
import {AuthenticationService} from './authentication.service';
import {Observable} from 'rxjs';
import {GroupsService} from './groups.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewChecked {
  title = 'Betman';
  active = 'group';

  isLoggedIn: Observable<boolean>;

  constructor(private authService: AuthenticationService, groupService: GroupsService) {

  }

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();

  }

  ngAfterViewChecked() {
  }


  logout() {
    this.authService.logout();
  }
}
