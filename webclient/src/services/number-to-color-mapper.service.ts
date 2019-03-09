import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NumberToColorMapperService {

  colors = [
    '#DDDDDD',
    '#0074D9',
    '#7FDBFF',
    '#39CCCC',
    '#3D9970',
    '#2ECC40',
    '#01FF70',
    '#FFDC00',
    '#E47A2E',
    '#F1EA7F',
    '#EAE6DA',
    '#ECDB54'
  ];

  constructor() { }

  mapNumberToColor(number: number): string {
    if (number == null) return 'white';
    return this.colors[number % this.colors.length];

    // return '#'+Math.random().toString(16).substr(-6); <- random color
  }
}
