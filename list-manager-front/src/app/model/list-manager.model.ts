import { Injectable } from "@angular/core";
import { Category } from "./category.model";
import { List } from "./list.model";
import { ListItem } from "./listitem.model";

@Injectable()
export class ListManger {
  public list: List = new List();
  public Saved: boolean = false;

  public setList(newList: List) {
    // this.list = newList;
    this.Saved = false;
    this.list = JSON.parse(JSON.stringify(newList))
  }

  public addItem(listItem: ListItem) {
    if (!this.list.items) {
      this.list.items = [];
    }
    this.list.items.push(listItem);
  }

  public removeItem(listItem: ListItem) {
    let idx = this.list.items.indexOf(listItem);
    this.list.items.splice(idx, 1);
  }

  public changeName(newName: string) {
    this.list.name = newName;
  }

  public changeCategory(newCategory: Category) {
    this.list.category = newCategory;
  }

  public clear() {
    this.list = new List();
  }
}

