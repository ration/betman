import {Injectable} from '@angular/core';
import {HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthenticationService} from './authentication.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthenticationService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.url.match(/(register|login|join)/)) {
      return next.handle(req);
    }
    const user = this.authService.currentUser();
    if (user && user.token) {
      const cloned = req.clone({headers: req.headers.set('Authorization', 'Bearer ' + user.token)});
      return next.handle(cloned);
    }
    return next.handle(req);
  }
}

export let AuthInterceptorProvider = {
  provide: HTTP_INTERCEPTORS,
  useClass: AuthInterceptor,
  multi: true
};
