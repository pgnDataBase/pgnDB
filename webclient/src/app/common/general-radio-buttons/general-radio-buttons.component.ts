import {AfterContentInit, Component, ContentChildren, OnDestroy, OnInit, QueryList} from '@angular/core';
import {RadioButtonComponent} from './radio-button/radio-button.component';
import {Subscription} from 'rxjs';

@Component({
  selector: 'chessdb-general-radio-buttons',
  templateUrl: './general-radio-buttons.component.html',
  styleUrls: ['./general-radio-buttons.component.scss']
})
export class GeneralRadioButtonsComponent implements OnInit, AfterContentInit, OnDestroy {

  constructor() {
  }

  @ContentChildren(RadioButtonComponent)
  radioButtons: QueryList<RadioButtonComponent>;
  private subscription: Subscription = new Subscription();

  ngOnInit() {
  }

  ngAfterContentInit() {
    let initiallyActive = this.radioButtons.filter(rb => rb.initiallyActive === true);
    if (initiallyActive.length > 0) {
      initiallyActive[0].chosen = true;
    } else {
      this.radioButtons.first.chosen = true;
    }
    this.radioButtons.forEach(rb => {
      let sub = rb.currentlyActiveButtonChangedByInput.subscribe((active) => {
        this.updateButtonsAfterClick(active);
      });
      let sub2 = rb.clickedOnButton.subscribe((clicked) => {
        this.updateButtonsAfterClick(clicked);
      });
      this.subscription.add(sub);
      this.subscription.add(sub2);
    });
  }

  updateButtonsAfterClick(chosenButton: RadioButtonComponent) {
    chosenButton.chosen = true;
    this.radioButtons.forEach(rb => {
      if (chosenButton !== rb) {
        rb.chosen = false;
      }
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
