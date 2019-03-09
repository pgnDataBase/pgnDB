import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

//  @REFACTOR - could be done in much clearer way than input actions, transclusion or whatever
// also, some clearer way to manipulate dropdown width

export class DropdownAction {
  name: string;
  display: string;
  icon?: string;
  disabled?: boolean;
}

@Component({
  selector: 'chessdb-action-dropdown',
  templateUrl: './action-dropdown.component.html',
  styleUrls: ['./action-dropdown.component.scss']
})
export class ActionDropdownComponent implements OnInit {

  @Input()
  mainIcon: string = 'fa-caret-down';
  @Input()
  actions: DropdownAction[] = [];
  @Output()
  actionSelected: EventEmitter<string> = new EventEmitter<string>();
  @Input()
  titleText: string = null;
  @Input()
  dropdownDisabled: boolean = false;
  actionsVisible = false;
  @Input()
  dropWidth: number;

  toggleActionsVisible() {
    this.actionsVisible = !this.actionsVisible;
  }

  constructor() {
  }

  ngOnInit() {
  }

  actionClicked(action: DropdownAction) {
    if (action.disabled == null || action.disabled === false) {
      this.actionSelected.next(action.name);
      this.actionsVisible = false;
    }
  }
}
