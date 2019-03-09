import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'errorCode'
})
export class ErrorCodePipe implements PipeTransform {

  transform(value: any, args?: any): any {
    return this.errorCodes(value) ? this.errorCodes(value) : "Unknown error";
  }

  errorCodes(value: string) {
    if (value === 'required') {
      return 'Field is required'
    }
    if (value === 'maxLength') {
      return 'Max length exceeded';
    }
    if (value.startsWith('minValue')) {
      let minValue = value.split(':')[1];
      return 'Value is too low. Min is ' + minValue;
    }
    if (value.startsWith('maxValue')) {
      let maxValue = value.split(':')[1];
      return 'Value is too high. Max is ' + maxValue;
    }
    if (value === 'minLength') {
      return 'Field too short';
    }
    if (value === 'usernameTaken') {
      return 'Username already taken';
    }
  }

}
