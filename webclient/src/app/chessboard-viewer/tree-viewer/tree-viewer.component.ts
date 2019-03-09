import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ServerService} from '../../../services/backend-communication/server.service';
import {ModelStateService} from '../../../services/state-holders/model-state.service';
import {TreeNodeRequest} from '../../model/tree/TreeNodeRequest';
import {Constants} from '../../model/Constants';
import {TreeNodes} from '../../model/tree/TreeNodes';
import {ModalMessageService} from '../../common/modal-message/modal-message.service';
import {TreeNode} from '../../model/tree/TreeNode';
import {Move} from '../../model/Move';
import {ChessDB} from '../../model/ChessDB';

@Component({
  selector: 'chessdb-tree-viewer',
  templateUrl: './tree-viewer.component.html',
  styleUrls: ['./tree-viewer.component.scss']
})
export class TreeViewerComponent implements OnInit {

  constructor(private server: ServerService,
              private modal: ModalMessageService,
              private model: ModelStateService) {
  }
  currentTreeDB: ChessDB = null;
  isChoosingDatabase: boolean = false;
  lastRequest: TreeNodeRequest = null;
  currentResponse: TreeNodes = null;
  isTreeBuilding: boolean = false;
  // holds current clicked nodes path to allow backwards browsing
  currentTreeNodePath: TreeNode[] = [];
  shouldShowEngines: boolean = false;

  @Input()
  initialMove: Move = null;
  @Input()
  currentMove: Move = null;
  @Output()
  changeChessboardPositionFromTree: EventEmitter<string> = new EventEmitter<string>();
  rememberedMove: Move = null;

  ngOnInit() {
    this.currentTreeDB = this.model.currentDB;
    if (this.initialMove == null) {
      this.refreshToStartPosition();
    } else {
      this.refreshToMove(this.initialMove);
    }
  }

  buildTreeClicked(includeVariants: boolean) {
    this.isTreeBuilding = true;
    this.server.buildTree(this.currentTreeDB.name, includeVariants).then((res) => {
      this.isTreeBuilding = false;
      this.refreshToStartPosition();
    }).catch(err =>
      this.modal.openModalMessageOnChessDBException(err.error));
  }

  refreshToMove(move: Move) {
    this.rememberedMove = move;
    let request = this.getBasicTreeNodeRequest();
    request.fen = move.fen;
    this.currentTreeNodePath = [];
    this.refreshForRequest(request);
  }

  refreshToStartPosition() {
    let request = this.getBasicTreeNodeRequest();
    request.fen = Constants.START_FEN;
    this.currentTreeNodePath = [];
    this.refreshForRequest(request);
  }

  requeryLastRequestForCurrentDb() {
    this.lastRequest.databaseName = this.currentTreeDB.name;
    this.refreshForRequest(this.lastRequest);
  }

  clickedOnNode(node: TreeNode) {
    this.currentTreeNodePath.push(node);
    this.goToNode(node, true);
  }

  goToNode(node: TreeNode, final: boolean) {
    if (final) {
      this.changeChessboardPositionFromTree.next(node.fen);
    } else {
      if (this.currentTreeNodePath.length > 1){
        this.changeChessboardPositionFromTree.next(this.currentTreeNodePath[this.currentTreeNodePath.length - 1].fen);
      } else if (this.currentTreeNodePath.length === 1) {
        this.changeChessboardPositionFromTree.next(this.currentTreeNodePath[0].fen);
      } else if (this.rememberedMove) {
        this.changeChessboardPositionFromTree.next(this.rememberedMove.fen);
      } else {
          this.refreshToStartPosition();
        }
    }
    let request = this.getBasicTreeNodeRequest();
    request.positionId = final ? node.finalPositionId : node.startPositionId;
    this.refreshForRequest(request);
  }

  refreshPositionBack() {
    let node = this.currentTreeNodePath.pop();
    this.goToNode(node, false);
  }

  refreshForRequest(request: TreeNodeRequest) {
    this.server.getTreeNodes(request).then(res => {
      this.lastRequest = request;
      this.currentResponse = res;
      this.currentResponse.isTreeActual = res.isTreeActual;
      this.currentResponse.isTreeWithVariants = res.isTreeWithVariants;
    }).catch(err => this.modal.openModalMessageOnChessDBException(err.error));
  }

  getBasicTreeNodeRequest(): TreeNodeRequest {
    // limit for now assumes it will fetch everything in one go... if there happens to be such a big variety
    // of positions that it won't be able to we'll have to introduce pagination
    return {
      databaseName: this.currentTreeDB.name,
      limit: 10000,
      offset: 0,
      positionId: null,
      fen: null
    };
  }

  onTreeDatabaseChange($event) {
    this.currentTreeDB = $event;
    this.isChoosingDatabase = false;
    // position id throught databases is broken
    // this.requeryLastRequestForCurrentDb();
    this.refreshToStartPosition();
  }


}
