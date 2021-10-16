import { Component } from "@angular/core";
import { ListManger } from "../../model/list-manager.model";
import { Location } from '@angular/common';
import { ListRepository } from "src/app/model/list.repository";
import { Category } from "src/app/model/category.model";
import { ItemStatus } from "src/app/model/itemstatus.model";
import { ListItem } from "src/app/model/listitem.model";
import { List } from "src/app/model/list.model";

@Component({
  selector: "list-edit",
  templateUrl: "list-edit.component.html"
})
export class ListEditComponent {
  public constructor(
    public listManager: ListManger,
    private repository: ListRepository,
    private location: Location) {

  }

  public Saved: boolean = false;

  public Saving: boolean = false;

  public get Categories(): Category[] {
    return this.repository.getCategories();
  }

  public get CategoryNames(): string[] {
    return this.repository.getCategories().map(c => c?.name ?? "undefined");
  }

  public get ItemStatuses(): ItemStatus[] {
    return this.repository.getItemStatuses();
  }

  public get ItemStatusNames(): string[] {
    return this.repository.getItemStatuses().map(s => s?.name ?? "undefined");
  }

  public get Items(): ListItem[] {
    console.log("List edit: items");
    console.log(this.listManager.list.items);
    console.log("List edit: itemStatuses");
    console.log(this.ItemStatuses)
    return this.listManager.list.items
    // .map(i => {
    //   let newStatus = new ItemStatus();
    //   newStatus.name = i.status?.name;
    //   i.status = newStatus;
    //   return i;
    // })
    .sort((a, b) => a.number - b.number);
  }

  public get List(): List {
    return this.listManager.list;
  }

  // public itemStatusChange(item: ListItem, itemStatus: ItemStatus) {
  //   console.log("itemStatusChange ");
  //   item.status = itemStatus;
  // }

  public addNewItem(): void {
    let item = new ListItem();
    let items = this.Items;
    if (items.length > 0) {
      item.number = items[items.length - 1].number + 1;
    }
    // item.status = this.ItemStatuses.find(is => is.name == "To Do")
    item.status = new ItemStatus()
    item.status.name = "To Do";
    this.listManager.addItem(item);
  }

  public removeItem(item: ListItem) {
    this.listManager.removeItem(item);
  }

  public goBack() {
    this.location.back();
  }

  public saveList() {
    console.log(this.listManager.list);
    this.Saving = true;
    this.repository.saveList(this.listManager.list).subscribe(res => {
      this.Saved = res;
      this.listManager.Saved = res;
      this.Saving = false;
    });
  }
}
