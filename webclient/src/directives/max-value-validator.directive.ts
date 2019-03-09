import { Directive, Input } from '@angular/core';
import { NG_VALIDATORS, Validator, FormControl } from '@angular/forms';

@Directive({
  selector: '[maxValue][formControlName],[maxValue][formControl],[maxValue][ngModel]',
  providers: [{provide: NG_VALIDATORS, useExisting: MaxValueValidatorDirective, multi: true}]
})
export class MaxValueValidatorDirective implements Validator {
  @Input()
  maxValue: number;

  validate(c: FormControl): {[key: string]: any} {
    let v = c.value;
    let validationKey = 'maxValue:' + this.maxValue;
    let result = {};
    result[validationKey] = true;
    return ( v > this.maxValue)? result : null;
  }
}
