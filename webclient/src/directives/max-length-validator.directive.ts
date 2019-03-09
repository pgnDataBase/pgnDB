import {Directive, forwardRef, Input} from '@angular/core';
import {FormControl, NG_VALIDATORS, Validator} from '@angular/forms';

@Directive({
  selector: '[maxLength][ngModel],[maxLength][formControl]',
  providers: [
    { provide: NG_VALIDATORS,
      useExisting: forwardRef(() => MaxLengthValidatorDirective), multi: true }
  ]
})
export class MaxLengthValidatorDirective implements Validator {
  @Input('maxLength') maxLength: string;

  constructor() {
  }

  validate(c: FormControl) {
    if (c != null && c.value != null && c.value.length > this.maxLength) {
      return {
        maxLength: {
          valid: false
        }
      }
    }
    return null;
  }
}
