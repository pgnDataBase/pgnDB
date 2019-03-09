import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {Move} from '../../../model/Move';
import {ChessGame} from '../../../model/ChessGame';
import {ClipboardCopierService} from '../../../../services/clipboard-copier.service';
import {GameEditionService} from '../../edition/game-edition.service';
import {VariantSwapUtil} from '../../edition/variant-swap-util';


export enum GameActionsName {
  FENCOPY = 'FENCOPY',
  COMMENTADD = 'COMMENTADD',
  MOVEDELETE = 'MOVEDELETE',
  OPENTREE = 'OPENTREE',
  VARIANTDELETE = 'VARIANTDELETE',
  SWAPVARTOLEFT = 'SWAPVARTOLEFT',
  SWAPVARTORIGHT = 'SWAPVARTORIGHT'
}

@Component({
  selector: 'chessdb-game-action-panel',
  templateUrl: './game-action-panel.component.html',
  styleUrls: ['./game-action-panel.component.scss']
})
export class GameActionPanelComponent implements OnInit, OnChanges {

  @Input()
  currentMove: Move = null;
  @Input()
  currentGame: ChessGame;
  @Input()
  isTreeVisible: boolean = false;
  @Output()
  openTreeOnMove: EventEmitter<Move> = new EventEmitter<Move>();
  @Output()
  displayEntitiesChanged: EventEmitter<Move[]> = new EventEmitter();
  restrictDisplayToVariant: boolean = false;

  moveActions = [
    {name: GameActionsName.FENCOPY, display: 'Copy FEN', icon: 'fa-copy', disabled: false},
    {name: GameActionsName.COMMENTADD, display: 'Add comment',icon: 'fa-comment', disabled: false},
    {name: GameActionsName.MOVEDELETE, display: 'Delete move',icon: 'fa-trash', disabled: false},
    {name: GameActionsName.OPENTREE, display: 'Statistics', icon: 'fa-bar-chart', disabled: false},
    {name: GameActionsName.VARIANTDELETE, display: 'Delete variant', icon: 'fa-trash', disabled: true}];

  swapVarToLeftAction = {name: GameActionsName.SWAPVARTOLEFT, icon: 'fa-chevron-left',
    display: 'Variant to left', disabled: false};
  swapVarToRightAction = {name: GameActionsName.SWAPVARTORIGHT, icon: 'fa-chevron-right',
    display: 'Variant to right', disabled: false};

  constructor(private clipboard: ClipboardCopierService, private gameEditor: GameEditionService) {
  }

  gameAction(actionName: GameActionsName) {
    return this.moveActions.find(a => a.name === actionName);
  }

  removeAction(actionName: GameActionsName) {
    this.moveActions = this.moveActions.filter(action => action.name !== actionName);
  }


  ngOnChanges(changes: SimpleChanges) {
    if (changes.currentMove && changes.currentMove.currentValue != null) {
      this.gameAction(GameActionsName.MOVEDELETE).disabled =
        !this.gameEditor.isDeleteAllowedForMove(changes.currentMove.currentValue, this.currentGame);
      this.gameAction(GameActionsName.COMMENTADD).disabled =
        changes.currentMove.currentValue.comment != null;
      this.gameAction(GameActionsName.VARIANTDELETE).disabled =
        changes.currentMove.currentValue.variantId == null;
      this.computeVariantsSwapAvailability();
    }
    if (changes.isTreeVisible) {
      this.moveActions.find(a => a.name === GameActionsName.OPENTREE).disabled = changes.isTreeVisible.currentValue;
    }
  }

  computeVariantsSwapAvailability() {
    if (VariantSwapUtil.canSwapToLeft(this.currentGame.moveList, this.currentMove)) {
      this.moveActions.push(this.swapVarToLeftAction);
    } else {
      this.removeAction(GameActionsName.SWAPVARTOLEFT);
    }

    if (VariantSwapUtil.canSwapToRight(this.currentGame.moveList, this.currentMove)) {
      this.moveActions.push(this.swapVarToRightAction);
    } else {
      this.removeAction(GameActionsName.SWAPVARTORIGHT);
    }
  }

  ngOnInit() {
  }

  changeRestrictionToVariants(toCurrent: boolean) {
    this.restrictDisplayToVariant = toCurrent;
    if (this.restrictDisplayToVariant) {
      if (this.currentMove != null) {
        this.displayEntitiesChanged.next(this.getEntitiesForPathOfCurrentVariant());
      } else {
        this.displayEntitiesChanged.next(this.currentGame.moveList.filter(mv => mv.variantId == null));
      }
    } else {
      this.displayEntitiesChanged.next(this.currentGame.moveList);
    }
  }

  copyFen() {
    this.clipboard.copy(this.currentMove.fen);
  }

  getEntitiesForPathOfCurrentVariant() {
    let variantIdsToInclude = [];
    let moreNestedVariantsToIgnore = [];
    let currentlyCheckedMove = this.currentMove;
    let initialMove = this.currentMove;
    // while we haven't stepped out of variants to main game
    while (!variantIdsToInclude.includes(null)) {
      if (currentlyCheckedMove == null) {
        break;
      }
      if (!variantIdsToInclude.includes(currentlyCheckedMove.variantId)) {
        // if we pass by end of variant move add it to ignored because it was more nested
        if ((currentlyCheckedMove.variantType === 'VE')
          && currentlyCheckedMove != initialMove
          && !moreNestedVariantsToIgnore.includes(currentlyCheckedMove.variantId)) {
          moreNestedVariantsToIgnore.push(currentlyCheckedMove.variantId);
        }
        variantIdsToInclude.push(currentlyCheckedMove.variantId);
      }
      let indexOfCurrentlyChecked = this.currentGame.moveList
        .findIndex(mv => mv === currentlyCheckedMove);
      currentlyCheckedMove = this.currentGame.moveList[indexOfCurrentlyChecked - 1];
    }
    let finalVariantsIdsToInclude = variantIdsToInclude
      .filter((vId) => !moreNestedVariantsToIgnore.includes(vId));
    let result = this.currentGame.moveList
      .filter(mv => finalVariantsIdsToInclude.includes(mv.variantId));
    return result;
  }

  moveActionSelected($event) {
    let isActionAllowed: boolean = this.moveActions.find(a => a.name === $event).disabled === false;
    if ($event === GameActionsName.FENCOPY && isActionAllowed) {
      this.copyFen();
    }
    if ($event === GameActionsName.MOVEDELETE && isActionAllowed) {
      this.gameEditor.moveDeletionAttemptHappened(this.currentGame, this.currentMove);
    }
    if ($event === GameActionsName.COMMENTADD && isActionAllowed) {
      this.currentMove.comment = '';
      this.gameAction(GameActionsName.COMMENTADD).disabled = true;
    }
    if ($event === GameActionsName.OPENTREE && isActionAllowed) {
      this.openTreeOnMove.next(this.currentMove);
    }
    if ($event === GameActionsName.VARIANTDELETE && isActionAllowed) {
      this.gameEditor.deleteVariant(this.currentGame, this.currentMove.variantId);
      this.gameAction(GameActionsName.VARIANTDELETE).disabled = true;
    }
    if ($event === GameActionsName.SWAPVARTOLEFT && isActionAllowed) {
      this.gameEditor.swapVariantToLeft(this.currentGame, this.currentMove);
      this.computeVariantsSwapAvailability();
    }
    if ($event === GameActionsName.SWAPVARTORIGHT && isActionAllowed) {
      this.gameEditor.swapVariantToRight(this.currentGame, this.currentMove);
      this.computeVariantsSwapAvailability();
    }
  }
}
