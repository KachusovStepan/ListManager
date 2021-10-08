import {
  Directive, ViewContainerRef, TemplateRef,
  Input, Attribute, SimpleChanges
} from "@angular/core";

@Directive({
  selector: "[counterOf]"
})
export class CounterDirective {
  public constructor(
    private container: ViewContainerRef,
    private template: TemplateRef<Object>
  ) {}

  @Input("counterOf")
  public counter: number = 0;

  ngOnChange(changes: SimpleChanges) {
    this.container.clear();
    for (let i = 0; i < this.counter; i++) {
      this.container.createEmbeddedView(this.template,
        new CounterDirectiveContext(i + 1));
    }
  }
}

class CounterDirectiveContext {
  // using implicit so we can use any variable name (# let someVar)
  constructor(public $implicit: any) {}
}
