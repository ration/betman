import {HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';

/**
 * Simple filter chain type interceptor
 */
export interface ApiFilter {
  filter(request: HttpRequest<any>): Observable<HttpResponse<any>>;
}
