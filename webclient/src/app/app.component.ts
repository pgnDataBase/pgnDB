import {Component, ComponentFactoryResolver, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {AuthenticationService, AuthEventType} from './authorization/authentication.service';
import {SettingsService} from './settings/settings.service';
import {ModalMessageService} from './common/modal-message/modal-message.service';
import {ModalDirective} from '../directives/modal.directive';
import {Subscription} from 'rxjs';
import {Constants} from './model/Constants';


@Component({
  selector: 'chessdb-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {

  constructor(private auth: AuthenticationService,
              private modal: ModalMessageService,
              private componentFactoryResolver: ComponentFactoryResolver,
              private settings: SettingsService) {}

  private subscription: Subscription = new Subscription();

  ngOnInit() {
    let sub4 = SettingsService.settingsLoadEnd.subscribe(() => {
      this.loggedIn = true;
    });
    this.subscription.add(sub4);
    let username = this.auth.getUsernameCookie();
    let token = this.auth.getTokenCookie();
    if(username != null && token != null && username.length > 0 && token.length > 0) {
      let serverIpCookie = this.auth.getServerIpCookie();
      if (serverIpCookie != null) {
        Constants.SERVER_IP = serverIpCookie;
        Constants.serverIpChanged.next();
      }

      this.auth.activeToken = token;
      this.auth.loggedInAs = username;
      this.settings.loadSettings();
    }

    let sub = this.modal.getModalSubjects().openModalSubject.subscribe(val => {
      this.renderModal(val.comp, val.data);
    });
    let sub2 = this.modal.getModalSubjects().closeModalSubject.subscribe(() => {
      this.closeModal();
    });

    let sub3 = this.auth.authEvents.subscribe((authEvent: AuthEventType) => {
      if (authEvent == AuthEventType.LOGIN) {
        this.settings.loadSettings();
      }
      if (authEvent == AuthEventType.LOGOUT) {
        this.loggedIn = false;
      }
    });

    this.subscription.add(sub);
    this.subscription.add(sub2);
    this.subscription.add(sub3);
  }

  @ViewChild(ModalDirective) modalTemplate: ModalDirective;
  renderModal(component, data) {
    let componentFactory = this.componentFactoryResolver.resolveComponentFactory(component);
    let viewContainerRef = this.modalTemplate.viewContainerRef;
    viewContainerRef.clear();
    if (component) {
      let ref = viewContainerRef.createComponent(componentFactory);
      // this is a bit hacky, but works
      Object.keys(data).forEach(key => {
        ref['_component'][key] = data[key];
      })
    }
  }

  closeModal() {
    this.modalTemplate.viewContainerRef.clear();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  loggedIn: boolean = false;
}
