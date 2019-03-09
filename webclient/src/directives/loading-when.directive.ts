import {ComponentFactoryResolver, ComponentRef, Directive, Input, TemplateRef, ViewContainerRef} from '@angular/core';
import {LoadingComponent} from '../app/common/loading/loading.component';


@Directive({selector: '[loadingWhen]'})
export class LoadingWhenDirective {

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private componentFactoryResolver: ComponentFactoryResolver) {
  }

  @Input() loadingWhenSize: string = '24px';
  @Input() loadingWhenColor: string = 'black';
  @Input() loadingWhenPadding: string = '0px';
  @Input() loadingWhenMsg: string = '';
  private spinnerRef: ComponentRef<LoadingComponent>;

  @Input() set loadingWhen(condition: boolean) {
    if (this.spinnerRef) {
      this.spinnerRef.destroy();
      this.spinnerRef = undefined;
    }
    if (condition) {
      this.setLoadingComponent();
    }
    else {
      this.setNormalComponent();
    }
  }

  setLoadingComponent() {
    let componentFactory = this.componentFactoryResolver.resolveComponentFactory(LoadingComponent);
    this.viewContainer.clear();
    let componentRef = this.viewContainer.createComponent(componentFactory);
    componentRef.instance.size = this.loadingWhenSize;
    componentRef.instance.color = this.loadingWhenColor;
    componentRef.instance.padding = this.loadingWhenPadding;
    componentRef.instance.loadingMessage = this.loadingWhenMsg;
    componentRef.instance.setStyles();
    this.spinnerRef = componentRef;
  }

  setNormalComponent() {
    this.viewContainer.clear();
    this.viewContainer.createEmbeddedView(this.templateRef);
  }
}
