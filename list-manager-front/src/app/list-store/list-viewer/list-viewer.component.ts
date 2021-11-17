import { Component } from "@angular/core";
import { ListManger } from "../../model/list-manager.model";
import { Location } from '@angular/common';
import { Router } from "@angular/router";

@Component({
  selector: "list-viewer",
  templateUrl: "list-viewer.component.html"
})
export class ListViewerComponent {
  public constructor(
    public listManager: ListManger,
    private location: Location,
    private router: Router) {

  }

  public goBack() {
    this.location.back();
  }

  public editList() {
    this.listManager.Saved = false;
    this.router.navigate(["list-store/list-edit"]);
  }


  // public goBack() {
  //   this.location.
  // }
}
