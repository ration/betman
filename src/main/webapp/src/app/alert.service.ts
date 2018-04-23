import {Injectable} from '@angular/core';
import {Router, NavigationStart} from '@angular/router';
import {Subject} from 'rxjs/Subject';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/delay';

@Injectable()
export class AlertService {
  private subject = new Subject<any>();
  private keepAfterNavigationChange = false;

  constructor(private router: Router) {
    // clear alert message on route change
    router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        if (this.keepAfterNavigationChange) {
          // only keep for a single location change
          this.keepAfterNavigationChange = false;
        } else {
          // clear alert
          this.subject.next();
        }
      }
    });
  }

  success(message: string, keepAfterNavigationChange = false, clearAfter: number = -1) {
    this.keepAfterNavigationChange = keepAfterNavigationChange;
    this.subject.next({type: 'success', text: message, clearAfter: clearAfter});
  }

  error(message: string, keepAfterNavigationChange = false) {
    this.keepAfterNavigationChange = keepAfterNavigationChange;
    this.subject.next({type: 'error', text: message});
  }

  getMessage(): Observable<any> {
    return this.subject.asObservable();
  }
}
