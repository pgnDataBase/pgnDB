import { Pipe, PipeTransform } from '@angular/core';

const DEFAULT_TRIM_LEN = 30;

@Pipe({
  name: 'trim'
})
export class TrimPipe implements PipeTransform {

  transform(value: string, toLength?: number): any {
    if (value) {
      let expectedLength = toLength ? toLength : DEFAULT_TRIM_LEN;
      if (expectedLength >= value.length) {
        return value;
      }
      else {
        if (value.length <= 3) {
          return value;
        }
        return value.substr(0, expectedLength - 3) + '...';
      }
    }
  }
}
