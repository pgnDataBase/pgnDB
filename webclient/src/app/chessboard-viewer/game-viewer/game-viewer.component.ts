import {Component, EventEmitter, HostListener, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges, ViewChild} from '@angular/core';
import {ChessGame} from '../../model/ChessGame';
import {Move} from '../../model/Move';
import {Bounds, PaginationService} from '../../../services/pagination/pagination.service';
import {VariantDepthDisplayCalculator} from './variant-depth-display-calculator';
import {SettingsService} from '../../settings/settings.service';
import {ClipboardCopierService} from '../../../services/clipboard-copier.service';
import {GameEditionService} from '../edition/game-edition.service';
import {Subscription} from 'rxjs';
import {GameActionPanelComponent, GameActionsName} from './game-action-panel/game-action-panel.component';

/**
 * The 'pipeline' of entities to display is as follows
 *  currentGame.moveList -> (if restricted to variant) | restriction to variant Path | pagination
 */


@Component({
  selector: 'chessdb-game-viewer',
  templateUrl: './game-viewer.component.html',
  styleUrls: ['./game-viewer.component.scss']
})
export class GameViewerComponent implements OnInit, OnDestroy {

  @Input()
  currentGame: ChessGame = null;
  @Input()
  currentMove: Move = null;
  @Output()
  entityClickedEmitter: EventEmitter<Move> = new EventEmitter<Move>();
  treeVisible: boolean = false;
  @Output()
  treeVisibleChanged: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output()
  openTreeOnMove: EventEmitter<Move> = new EventEmitter<Move>();

  // entities to display can be restricted eg to given "variant path"
  entitiesToDisplay: Move[] = [];
  // those are actually displayed, it is the last array in "pipeline"
  entitiesToDisplayPaginated: Move[] = [];
  entitiesPageSize: number = 1000000;

  showEarlierMovesVisible: boolean = true;
  showLaterMovesVisible: boolean = true;

  private subscription: Subscription = new Subscription();

  @ViewChild(GameActionPanelComponent) gameActionPanel: GameActionPanelComponent;

  @HostListener('window:keydown.delete')
  onDeletePressed() {
    if (this.currentMove != null) {
      this.gameActionPanel.moveActionSelected(GameActionsName.MOVEDELETE);
    }
  }

  variantDepthMap = {};

  constructor(private pagination: PaginationService,
              private clipboard: ClipboardCopierService,
              private gameEditor: GameEditionService,
              private settings: SettingsService) {
  }

  ngOnInit() {
    this.entitiesPageSize = this.pagination.getPageSizeByContextName('entities');
    this.entitiesToDisplay = this.currentGame.moveList;
    this.updatePaginationContextForEntities();
    this.updateEntitesToDisplayPaginated();
    this.updateShowMoreVisibility();
    this.variantDepthMap = VariantDepthDisplayCalculator.calculateForMoveList(this.currentGame.moveList);
    this.subToEditor();
  }

  subToEditor() {
    let sub = this.gameEditor.moveHappened.subscribe(move => this.moveHappened(move));
    let sub2 = this.gameEditor.reloadDisplay.subscribe(() => {
      this.gameActionPanel.restrictDisplayToVariant = false;
      this.entitiesToDisplay = this.currentGame.moveList;
      this.variantDepthMap = VariantDepthDisplayCalculator.calculateForMoveList(this.currentGame.moveList);
      this.updatePaginationContextForEntities();
      this.updateEntitesToDisplayPaginated();
      this.updateShowMoreVisibility();
    });
    let sub3 = this.gameEditor.reloadToMove.subscribe((move: Move) => this.onEntityClicked(move));
    this.subscription.add(sub);
    this.subscription.add(sub2);
    this.subscription.add(sub3);
  }

  moveHappened(move) {
    this.currentMove = move;
    this.onEntityClicked(move);
  }

  onEntityClicked(move: Move) {
    this.entityClickedEmitter.next(move);
    if (this.settings.settingsValues.GENERIC_copyFenOnMove) {
      this.copyToClipboard(move.fen);
    }
  }

  copyToClipboard(val) {
    this.clipboard.copy(val);
  }

  onDisplayEntitiesChanged($event) {
    this.entitiesToDisplay = $event;
    this.updatePaginationContextForEntities();
    this.updateEntitesToDisplayPaginated();
    this.updateShowMoreVisibility();
  }

  updatePaginationContextForEntities() {
    this.pagination.getContext('entities').currentPage = 0;
    this.pagination.getContext('entities').itemsTotal = this.entitiesToDisplay.length;
  }

  showLaterMovesClicked() {
    if (this.pagination.canIncreasePage('entities')) {
      this.pagination.getContext('entities').currentPage++;
      this.updateEntitesToDisplayPaginated();
      this.updateShowMoreVisibility();
    }
  }

  showEarlierMovesClicked() {
    if (this.pagination.canDecreasePage('entities')) {
      this.pagination.getContext('entities').currentPage--;
      this.updateEntitesToDisplayPaginated();
      this.updateShowMoreVisibility();
    }
  }

  updateShowMoreVisibility() {
    this.showLaterMovesVisible = this.pagination.canIncreasePage('entities');
    this.showEarlierMovesVisible = this.pagination.canDecreasePage('entities');
  }

  updateEntitesToDisplayPaginated() {
    let ctx = this.pagination.getContext('entities');
    let items: Bounds = this.pagination.itemsThatAreOnGivenPage(ctx.currentPage, ctx.pageSize);
    this.entitiesToDisplayPaginated = this.entitiesToDisplay.slice(items.lowerBound, items.upperBound);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  onOpenTreeOnMove(move: Move) {
    this.treeVisible = true;
    this.treeVisibleChanged.next(true);
    this.openTreeOnMove.next(move);
  }

  tempFillerTable = [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1];

}
