import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Constants} from '../../app/model/Constants';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  baseUrl = 'http://' + Constants.SERVER_IP + '/api';

  constructor(private http: HttpClient) {
    Constants.serverIpChanged.subscribe(() => this.recalculateBaseUrl());
  }

  recalculateBaseUrl() {
    this.baseUrl = 'http://' + Constants.SERVER_IP + '/api';
  }

  deleteAccount(password: string): Promise<any> {
    return this.http.post(this.baseUrl + '/user/remove-account', {
      password: password
    }).toPromise();
  }

  checkUsernameAvaiability(username: string): Promise<any> {
    return this.http.post(this.baseUrl + '/username/availability', {
      username: username
    }).toPromise();
  }
}
