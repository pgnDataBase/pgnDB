import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ServerService} from '../../../../../services/backend-communication/server.service';
import {ModelStateService} from '../../../../../services/state-holders/model-state.service';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';

// @TODO (OPTIONAL) if there is enough time it should probably cache queries, and be more generic

@Component({
  selector: 'chessdb-header-autocomplete-input',
  templateUrl: './header-autocomplete-input.component.html',
  styleUrls: ['./header-autocomplete-input.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: HeaderAutocompleteInputComponent,
    multi: true
  }]
})
export class HeaderAutocompleteInputComponent implements OnInit, ControlValueAccessor {

  constructor(private server: ServerService, private model: ModelStateService) { }

  innerValue: string = '';
  autoCompletionList = [];
  minAutoCompletionLength = 1;
  @Output() valueChanged: EventEmitter<string> = new EventEmitter();

  ngOnInit() {
  }

  temp() {
    if (this.model.currentDB != null && this.innerValue.length >= this.minAutoCompletionLength) {
      this.server.getAvailableHeaders(this.model.currentDB.name, this.innerValue)
        .then(res => this.autoCompletionList = res)
        .catch(err => this.autoCompletionList = []);
    }
  }

  itemSelected(item) {
    this.innerValue = item;
    this.innerValueChange(item);
    this.autoCompletionList = [];
  }

  isAutocompletionItemNotLast(item) {
    return this.autoCompletionList.findIndex(it => it == item) != this.autoCompletionList.length - 1;
  }

  get value(): any {
    return this.innerValue;
  }

  private onTouchedCallback: () => void = () => {};
  private onChangeCallback: (_: any) => void = () => {};


  registerOnChange(fn: any): void {
    this.onChangeCallback = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouchedCallback = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    // nothing
  }

  writeValue(obj: any): void {
    this.innerValue = obj;
  }

  innerValueChange(value) {
    this.onChangeCallback(value);
    this.valueChanged.next(value);
  }

}
