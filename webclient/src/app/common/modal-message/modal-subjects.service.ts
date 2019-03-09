import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ModalSubjectsService {

  constructor() { }

  openModalSubject: Subject<{comp: any, data: any}> = new Subject<{comp: any, data: any}>();
  closeModalSubject: Subject<void> = new Subject<void>();
}
