import {Directive, forwardRef, Input} from '@angular/core';
import {FormControl, NG_VALIDATORS, Validator} from '@angular/forms';

@Directive({
  selector: '[minLength][ngModel],[minLength][formControl]',
  providers: [
    { provide: NG_VALIDATORS,
      useExisting: forwardRef(() => MinLengthValidatorDirective), multi: true }
  ]
})
export class MinLengthValidatorDirective implements Validator {
  @Input('minLength') minLength: string;

  constructor() {
  }

  validate(c: FormControl) {
    if (c != null && c.value != null && c.value.length < this.minLength) {
      return {
        minLength: {
          valid: false
        }
      }
    }
    return null;
  }
}
