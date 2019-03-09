import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[modal]',
})
export class ModalDirective {
  constructor(public viewContainerRef: ViewContainerRef) { }
}
