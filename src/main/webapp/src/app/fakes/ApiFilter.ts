import {HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';

export interface ApiFilter {
  filter(request: HttpRequest<any>): Observable<HttpResponse<any>>;
}
