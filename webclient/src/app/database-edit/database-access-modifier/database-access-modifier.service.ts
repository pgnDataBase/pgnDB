import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Constants} from '../../model/Constants';

@Injectable({
  providedIn: 'root'
})
export class DatabaseAccessModifierService {

  constructor(private http: HttpClient) {
    Constants.serverIpChanged.subscribe(() => this.recalculateBaseUrl());
  }

  private baseUrl: string = Constants.SERVER_IP + '/api/';

  recalculateBaseUrl() {
    this.baseUrl = Constants.SERVER_IP + '/api/';
  }

  giveUserDbPermission(databaseId: string, username: string) {
    return this.http.post(this.baseUrl + 'chessdb/access', {
      databaseId: databaseId,
      username: username
    }).toPromise();
  }

  searchUsers(value: string) {
    return this.http.post(this.baseUrl + 'search/users', {
      value: value
    }).toPromise();
  }

  usersThatHavePermissionToDb(databaseId: string) {
    return this.http.post(this.baseUrl + 'chessdb/access/users', {
      databaseId: databaseId
    }).toPromise();
  }

  removePermission(databaseId: string, username: string) {
    return this.http.post(this.baseUrl + 'chessdb/access/remove', {
      databaseId: databaseId,
      username: username
    }).toPromise();
  }

}
