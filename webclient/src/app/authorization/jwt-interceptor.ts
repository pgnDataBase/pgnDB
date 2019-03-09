import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthenticationService} from './authentication.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private auth: AuthenticationService){}
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let modified = request;
    if(this.auth.activeToken !== null) {
      modified = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.auth.activeToken}`
        }
      });
    }
    return next.handle(modified);
    // obs.toPromise().catch(err => {
    //   this.auth.clearCredentials();
    //   this.auth.authEvents.next(AuthEventType.LOGOUT);
    // });
  }
}
