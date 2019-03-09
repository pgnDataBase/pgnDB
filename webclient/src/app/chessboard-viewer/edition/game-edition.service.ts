import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {Move} from '../../model/Move';
import {ChessGame} from '../../model/ChessGame';
import {ServerService} from '../../../services/backend-communication/server.service';
import {ChessDBException} from '../../model/ChessDBException';
import {DeletionHelperUtil} from './deletion-helper-util';
import {PiecePromotionService} from './piece-promotion/piece-promotion.service';
import {PromotionModalService} from './piece-promotion/promotion-modal.service';
import {AdditionHelperUtil} from './addition-helper-util';
import {VariantSwapUtil} from './variant-swap-util';

@Injectable({
  providedIn: 'root'
})
export class GameEditionService {

  constructor(private server: ServerService,
              private promotionModal: PromotionModalService,
              private piecePromotionService: PiecePromotionService) {
  }

  moveHappened: Subject<Move> = new Subject<Move>();
  reloadDisplay: Subject<void> = new Subject();
  reloadToMove: Subject<Move> = new Subject<Move>();
  moveValidationErrorEvents: Subject<ChessDBException> = new Subject<ChessDBException>();

  moveCreationAttemptHasHappened(currentGame: ChessGame, currentMove: Move, newMove: Move) {
    this.server.newMoveCreation(currentMove, newMove).then(res => {
      let resultMoveFromServer = res as Move;
      if (resultMoveFromServer.promotionAllowed) {
        this.handleMovePromotion(currentMove, currentGame, newMove);
      } else {
        this.moveCreationOnServerSucceded(resultMoveFromServer, currentGame, currentMove);
      }
    }).catch(err => this.moveCreationFailed(err, currentMove));
  }

  handleMovePromotion(currentMove: Move, currentGame: ChessGame, newMove: Move) {
    this.promotionModal.openPromotionModal(currentMove.pieceCode.substr(0, 1));
    let tempSub = this.piecePromotionService.piecePromotionSubject.subscribe(pieceCode => {
      newMove.promotedPieceCode = pieceCode;
      this.server.newMoveCreation(currentMove, newMove).then(res => {
        let promotedMove = res as Move;
        this.moveCreationOnServerSucceded(promotedMove, currentGame, currentMove);
      }).catch(err => this.moveCreationFailed(err, currentMove));
      tempSub.unsubscribe();
    });
  }

  moveCreationOnServerSucceded(resultMoveFromServer: Move, currentGame: ChessGame, currentMove: Move) {
    let insertedMove = AdditionHelperUtil.properlyInsertMove(currentGame, currentMove, resultMoveFromServer);
    // causes focus on move
    this.moveHappened.next(insertedMove);
    this.reloadDisplay.next();
  }

  moveCreationFailed(err, currentMove: Move) {
    this.moveValidationErrorEvents.next(err.error);
    this.reloadToMove.next(currentMove);
  }

  moveDeletionAttemptHappened(currentGame: ChessGame, currentMove: Move) {
    DeletionHelperUtil.deleteDependentVariantsForMove(currentGame, currentMove);
    let index = currentGame.moveList.findIndex(mv => mv === currentMove);
    currentGame.moveList.splice(index, 1);
    // update of variant type of move before
    if (currentGame.moveList.length > 0 && currentMove.variantId != null) {
      let reloadTo =
        DeletionHelperUtil.updateVariantsOnMoveDeletion(currentGame, currentMove);
      this.reloadToMove.next(reloadTo);
    } else if (currentGame.moveList.length > 0) {
      let noVariant = currentGame.moveList.filter(mv => mv.variantId == null);
      this.reloadToMove.next(noVariant[noVariant.length - 1]);
    } else {
      this.reloadToMove.next(null);
    }
    this.reloadDisplay.next();
  }

  isDeleteAllowedForMove(move: Move, game: ChessGame): boolean {
    if (move == null) {
      return false;
    }
    let noVariant = game.moveList.filter(mv=> mv.variantId == null);
    if (noVariant[noVariant.length - 1] === move) {
      return true;
    }
    let withSameVariant = game.moveList.filter(mv => mv.variantId === move.variantId);
    if (withSameVariant[withSameVariant.length - 2] === move) {
      return true;
    }
    return false;
  }

  deleteVariant(currentGame: ChessGame, variantId: number) {
    DeletionHelperUtil.deleteVariant(currentGame, variantId);
    this.reloadDisplay.next();
  }

  swapVariantToLeft(currentGame: ChessGame, currentMove: Move) {
    let leftNeighbourVariantId = VariantSwapUtil.getLeftNeighbourVariantId(currentGame.moveList, currentMove);
    console.log(leftNeighbourVariantId);
    VariantSwapUtil.swapNeighbourVariants(currentGame.moveList, currentMove.variantId, leftNeighbourVariantId);
    this.reloadDisplay.next();
  }

  swapVariantToRight(currentGame: ChessGame, currentMove: Move) {
    let rightNeighbourVariantId = VariantSwapUtil.getRightNeighbourVariantId(currentGame.moveList, currentMove);
    VariantSwapUtil.swapNeighbourVariants(currentGame.moveList, currentMove.variantId, rightNeighbourVariantId);
    this.reloadDisplay.next();
  }
}
