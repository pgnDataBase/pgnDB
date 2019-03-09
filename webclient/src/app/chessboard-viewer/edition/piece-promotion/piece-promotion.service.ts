import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';

// @REFACTOR maybe would be clearer as injection token?

@Injectable({
  providedIn: 'root'
})
export class PiecePromotionService {
  piecePromotionSubject: Subject<string> = new Subject<string>();
}
