import {AfterViewChecked, AfterViewInit, Component, OnInit} from '@angular/core';
import {AuthenticationService} from './authentication.service';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewChecked {
  title = 'Betman';

  isLoggedIn: Observable<boolean>;

  constructor(private authService: AuthenticationService) {

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
