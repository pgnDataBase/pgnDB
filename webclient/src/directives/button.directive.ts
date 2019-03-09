import {Directive, ElementRef, Input, OnInit, Renderer2} from '@angular/core';

@Directive({
  selector: '[chessdbButton]'
})
export class ButtonDirective implements OnInit {

  @Input() square: boolean = false;
  @Input() hasLeftNeighbour: boolean = false;
  @Input() hasRightNeighbour: boolean = false;
  @Input() width: string = null;
  @Input() noMargin: boolean = false;

  constructor(private elRef: ElementRef, private renderer: Renderer2) {

  }

  ngOnInit() {
    if (this.square === true) {
      this.renderer.setStyle(this.elRef.nativeElement, 'border-radius', '0px');
    }
    if (this.hasLeftNeighbour === true) {
      this.renderer.setStyle(this.elRef.nativeElement, 'margin-left', '0px');
      this.renderer.setStyle(this.elRef.nativeElement, 'border-top-left-radius', '0px');
      this.renderer.setStyle(this.elRef.nativeElement, 'border-bottom-left-radius', '0px');
    }
    if (this.hasRightNeighbour === true) {
      this.renderer.setStyle(this.elRef.nativeElement, 'margin-right', '0px');
      this.renderer.setStyle(this.elRef.nativeElement, 'border-top-right-radius', '0px');
      this.renderer.setStyle(this.elRef.nativeElement, 'border-bottom-right-radius', '0px');
    }
    if (this.width !== null) {
      this.renderer.setStyle(this.elRef.nativeElement, 'width', this.width);
    }
    if (this.noMargin) {
      this.renderer.setStyle(this.elRef.nativeElement, 'margin', '0px');
    }
  }

}
