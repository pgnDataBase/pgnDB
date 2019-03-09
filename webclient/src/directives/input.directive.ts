import {Directive, ElementRef, Renderer2} from '@angular/core';

@Directive({
  selector: '[chessdbInput]'
})
export class InputDirective {

  constructor(private elRef: ElementRef, private renderer: Renderer2) { }

}
