import { Component } from "@angular/core";
import { ListManger } from "../../model/list-manager.model";
import { List } from "../../model/list.model";

@Component({
  selector: "list-summary",
  templateUrl: "list-summary.component.html"
})
export class ListSummaryComponent {
  public constructor(public listManager: ListManger) {

  }
}
