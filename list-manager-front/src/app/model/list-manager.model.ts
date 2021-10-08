import { Injectable } from "@angular/core";
import { Category } from "./category.model";
import { List } from "./list.model";
import { ListItem } from "./listitem.model";

@Injectable()
export class ListManger {
  public list: List = new List();

  public setList(newList: List) {
    this.list = newList;
  }

  public addItem(listItem: ListItem) {
    if (!this.list.items) {
      this.list.items = [];
    }
    this.list.items.push(listItem);
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

