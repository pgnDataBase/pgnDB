import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticationService, AuthEventType} from '../authorization/authentication.service';
import {ModalMessageService} from '../common/modal-message/modal-message.service';
import {UserService} from '../../services/backend-communication/user.service';
import {Subject, Subscription} from 'rxjs';
import {debounceTime} from 'rxjs/operators';
import {Constants} from '../model/Constants';

@Component({
  selector: 'chessdb-login-screen',
  templateUrl: './login-screen.component.html',
  styleUrls: ['./login-screen.component.scss']
})
export class LoginScreenComponent implements OnDestroy {

  constructor(private auth: AuthenticationService,
              private userService: UserService,
              private modal: ModalMessageService) {
    const sub = this.loginKeyUp.pipe(
      debounceTime(300)
    ).subscribe(v => {
      this.checkLoginAvailable(v);
    });
    this.subscription.add(sub);
  }

  private subscription: Subscription = new Subscription();
  changingServerIp: boolean = false;


  createAccount: boolean = false;
  showAboutScreen: boolean = false;
  isPerformingAction: boolean = false;
  loginAvailable: boolean = true;

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  clickedConfirm(loginFormValue) {
    this.isPerformingAction = true;
    if (!this.createAccount) {
      this.login(loginFormValue);
    } else {
      this.register(loginFormValue);
    }
  }

  login(loginFormValue) {
    this.auth.login(loginFormValue).then((data: any) => {
      this.auth.activeToken= data.token;
      this.auth.deleteCookies();
      this.auth.setCookies(data.token, loginFormValue.username);
      this.auth.loggedInAs = loginFormValue.username;
      this.auth.authEvents.next(AuthEventType.LOGIN);
      this.isPerformingAction = false;
    }).catch(err => {
      this.isPerformingAction = false;
      this.modal.openModalMessageOnChessDBException(err.error);
    });
  }

  register(loginFormValue) {
    this.auth.createAccount(loginFormValue).then(res => {
      this.createAccount = false;
      this.isPerformingAction = false;
      this.auth.loginWithoutHttp(res.token, loginFormValue);
    }).catch(err => {
      this.isPerformingAction = false;
      this.modal.openModalMessageOnChessDBException(err.error);
    });
  }

  public loginKeyUp = new Subject<string>();

  checkLoginAvailable(login) {
    this.userService.checkUsernameAvaiability(login)
      .then(res => this.loginAvailable = res);
  }

  changeServerIp(value) {
    Constants.SERVER_IP = value;
    Constants.serverIpChanged.next();
    this.changingServerIp = false;
  }

}
