import {Injectable, OnDestroy} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Subject} from 'rxjs';
import {CookieService} from 'ngx-cookie-service';
import {Router} from '@angular/router';
import {ROUTING_PATHS} from '../../routing/routing-paths';
import {ModelStateService} from '../../services/state-holders/model-state.service';
import {Constants} from '../model/Constants';

export enum AuthEventType {
  LOGIN, LOGOUT
}

@Injectable({
  providedIn: 'root'
})

export class AuthenticationService implements OnDestroy {

  activeToken: string = null;
  loginRequest: any = null;
  authApiBase: string = 'http://' + Constants.SERVER_IP + '/api/user';
  loggedInAs: string = null;

  authEvents: Subject<AuthEventType> = new Subject<AuthEventType>();

  recalculateBaseUrl() {
    this.authApiBase = 'http://' + Constants.SERVER_IP + '/api/user';
  }

  constructor(private http: HttpClient,
              private router: Router,
              private model: ModelStateService,
              private cookieService: CookieService,) {
    Constants.serverIpChanged.subscribe(() => this.recalculateBaseUrl());
    // every 180 minutes refresh authorization
    // maybe could be done better, without interval?
    this.refreshTokenInterval = setInterval(() => {
      this.refreshToken();
    }, 180 * 60 * 1000);
  }

  refreshTokenInterval;

  login(loginRequest) {
    this.loginRequest = loginRequest;
    return this.http.post(this.authApiBase + '/sign-in', loginRequest).toPromise();
  }


  logout() {
    return this.http.post(this.authApiBase + '/sign-out',null).toPromise();
  }

  logoutAction() {
    this.logout().then(r => {
      this.authEvents.next(AuthEventType.LOGOUT);
      this.model.reset();
      this.router.navigate([ROUTING_PATHS.DEFAULT]);
      this.deleteCookies();
      this.clearCredentials();
    }).catch(err => {
      this.authEvents.next(AuthEventType.LOGOUT);
      this.deleteCookies();
      this.clearCredentials();
    })
  }

  logoutWithoutHttp() {
    this.model.reset();
    this.authEvents.next(AuthEventType.LOGOUT);
    this.router.navigate([ROUTING_PATHS.DEFAULT]);
    this.clearCredentials();
    this.deleteCookies();
  }

  loginWithoutHttp(token: string, loginFormValue) {
    this.activeToken = token;
    this.loggedInAs = loginFormValue.username;
    this.authEvents.next(AuthEventType.LOGIN);
  }


  clearCredentials() {
    this.activeToken = null;
    this.loginRequest = null;
    this.loggedInAs = null;
  }

  createAccount(loginFormValue): Promise<any> {
    return this.http.post(this.authApiBase + '/sign-up', loginFormValue).toPromise();
  }

  USERNAME_COOKIE_NAME = 'pgndbusername';
  TOKEN_COOKIE_NAME = 'pgndbtoken';
  SERVER_IP_COOKIE_NAME = 'pgndbserverip';



  getUsernameCookie() {
    return this.cookieService.get(this.USERNAME_COOKIE_NAME);
  }

  getTokenCookie() {
    return this.cookieService.get(this.TOKEN_COOKIE_NAME);
  }

  getServerIpCookie() {
    return this.cookieService.get(this.SERVER_IP_COOKIE_NAME);
  }

  setCookies(token, username) {
    let date = new Date();
    // for now 3 hour validity
    date.setTime(date.getTime() + (3 * 60 * 60 *1000));
    this.cookieService.set(this.TOKEN_COOKIE_NAME, token, date);
    this.cookieService.set(this.USERNAME_COOKIE_NAME, username);
    this.cookieService.set(this.SERVER_IP_COOKIE_NAME, Constants.SERVER_IP);
  }

  deleteCookies() {
    this.cookieService.delete(this.TOKEN_COOKIE_NAME);
    this.cookieService.delete(this.USERNAME_COOKIE_NAME);
  }

  refreshToken() {
    this.http.get(this.authApiBase + '/token').toPromise().then(res => {
      this.activeToken = res['token'] as string;
      this.setCookies(this.activeToken, this.loggedInAs);
    })
  }

  ngOnDestroy() {
    if (this.refreshTokenInterval) {
      clearInterval(this.refreshTokenInterval);
    }
  }

}
