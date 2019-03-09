import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {StartEngineRequest} from './engines-model';
import {Constants} from '../../model/Constants';

@Injectable({
  providedIn: 'root'
})
export class ChessEnginesService {

  constructor(private http: HttpClient) {
    Constants.serverIpChanged.subscribe(() => this.recalculateBaseUrl());
  }

  recalculateBaseUrl() {
    this.baseUrl = 'http://' + Constants.SERVER_IP + '/api/';
  }

  /**
   * API FOR THIS IS RETARDED, POSTS WHERE THERE SHOULD BE GETS NOT STANDARDIZED
   * URLS BEGINNING, AND MOSTLY NOT WORKING, SORRY I DID NOT DESIGN IT
   */

  baseUrl = 'http//' + Constants.SERVER_IP + '/api/';

  getEnginesList(): Promise<any> {
    return this.http.post(this.baseUrl + 'engines', {}).toPromise();
  }

  getEngineResult(): Promise<any> {
    return this.http.post(this.baseUrl + 'engine/result', null).toPromise();
  }

  stopEngine(): Promise<any> {
    return this.http.post(this.baseUrl + 'engine/stop', null).toPromise();
  }

  startEngine(startRequest: StartEngineRequest): Promise<any> {
    return this.http.post(this.baseUrl + 'engine/evaluate', startRequest).toPromise();
  }

}
