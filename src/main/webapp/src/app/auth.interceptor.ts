import {Injectable} from '@angular/core';
import {HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthenticationService} from './authentication.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private checkConnection = true;

  constructor(private authService: AuthenticationService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.verifyUser();
    if (req.url.match(/(register|login)/)) {
      return next.handle(req);
    }
    const user = this.authService.currentUser();
    if (user && user.token) {
      const cloned = req.clone({headers: req.headers.set('Authorization', 'Bearer ' + user.token)});
      return next.handle(cloned);
    }
    return next.handle(req);
  }

  /**
   Verify the user on first boot. Backend might've changed under
   */
  private verifyUser() {
    if (this.checkConnection) {
      this.checkConnection = false;
      if (this.authService.currentUser()) {
        this.authService.checkConnection();
      }
    }
  }
}

export let AuthInterceptorProvider = {
  provide: HTTP_INTERCEPTORS,
  useClass: AuthInterceptor,
  multi: true
};
