import {Component, ElementRef, Input, OnInit, Renderer2, ViewChild} from '@angular/core';

@Component({
  selector: 'chessdb-loading',
  templateUrl: './loading.component.html',
  styleUrls: ['./loading.component.scss']
})
export class LoadingComponent implements OnInit {

  @Input() size: string = '48px';
  @Input() color: string = 'black';
  @Input() padding: string = '0px';
  @Input() translateX: string = '0px';
  @Input() translateY: string = '0px';
  @Input() loadingMessage: string = '';

  constructor(private elRef: ElementRef, private renderer: Renderer2) { }

  ngOnInit() {
    this.setStyles();
  }

  setStyles() {
    this.renderer.setStyle(
      this.elRef.nativeElement, 'font-size', this.size);
    this.renderer.setStyle(
      this.elRef.nativeElement, 'color', this.color);
    this.renderer.setStyle(
      this.elRef.nativeElement, 'padding', this.padding);
    this.renderer.setStyle(
      this.elRef.nativeElement, 'transform',
      'translate(' + this.translateX + ','  + this.translateY + ')'
    )
  }

}
