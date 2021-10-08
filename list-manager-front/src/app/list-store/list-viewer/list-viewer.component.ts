import { Component } from "@angular/core";
import { ListManger } from "../../model/list-manager.model";
import { Location } from '@angular/common';

@Component({
  selector: "list-viewer",
  templateUrl: "list-viewer.component.html"
})
export class ListViewerComponent {
  public constructor(
    public listManager: ListManger,
    private location: Location) {

  }

  public goBack() {
    this.location.back();
  }
}
