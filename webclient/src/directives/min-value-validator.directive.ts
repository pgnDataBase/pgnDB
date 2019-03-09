import { Directive, Input } from '@angular/core';
import { NG_VALIDATORS, Validator, FormControl } from '@angular/forms';

@Directive({
  selector: '[minValue][formControlName],[minValue][formControl],[minValue][ngModel]',
  providers: [{provide: NG_VALIDATORS, useExisting: MinValueValidatorDirective, multi: true}]
})
export class MinValueValidatorDirective implements Validator {
  @Input()
  minValue: number;

  validate(c: FormControl): {[key: string]: any} {
    let v = c.value;
    let validationKey = 'minValue:' + this.minValue;
    let result = {};
    result[validationKey] = true;
    return ( v < this.minValue)? result : null;
  }
}
