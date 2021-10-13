import { Injectable } from "@angular/core";
import { List } from "./list.model";
import { Category } from "./category.model";
import { ListItem } from "./listitem.model";
import { ItemStatus } from "./itemstatus.model";

import { Observable, from } from "rxjs";
import { delay } from 'rxjs/internal/operators';

import { IUser } from "./user";

@Injectable()
export class StaticDataSource {
  private categories: Category[] = [
    new Category(1, "To Do list"),
    new Category(2, "Grocery list"),
    new Category(3, "Classes"),
  ];
  private itemsStatuses: ItemStatus[] = [
    new ItemStatus(1, "To Do"),
    new ItemStatus(2, "In Progress"),
    new ItemStatus(3, "Done")
  ];
  private listItems: ListItem[] = [
    new ListItem(1, 1, 1, "Buy Flowers", this.itemsStatuses[2] ),
    new ListItem(2, 2, 1, "Find Shoes", this.itemsStatuses[1] ),
    new ListItem(3, 1, 1, "Call Joe", this.itemsStatuses[0] ),
  ];
  private lists: List[] = [
    new List(1, "Adam's List", this.categories[0], [this.listItems[0], this.listItems[1], this.listItems[2]]),
    new List(2, "My List 1", this.categories[1], [this.listItems[0], this.listItems[1]]),
    new List(3, "My List 2", this.categories[0], [this.listItems[0], this.listItems[2]]),
    new List(4, "My List 3", this.categories[2], [this.listItems[0], this.listItems[1]]),
    new List(5, "My List 4", this.categories[1], [this.listItems[0], this.listItems[2]]),
    new List(6, "My List 5", this.categories[2], [this.listItems[0], this.listItems[1]]),
  ];

  public auth_token: string = "";
  public user: IUser = {
    id: 1,
    username: "Adam",
    email: "a@a.com",
    lists: [this.lists[0]]
  };

  public getLists(): Observable<List[]> {
    return from([this.lists]);
  }

  public getItemStatus(): Observable<ItemStatus[]> {
    return from([this.itemsStatuses]);
  }

  public getCategories(): Observable<ItemStatus[]> {
    return from([this.categories]);
  }

  public getUser(): Observable<IUser> {
    return from([this.user]);
  }

  public authenticate(user: string, pass: string): Observable<boolean> {
    return from([user == "Adam" && pass == "1234"]);
  }

  public register(username: string, pass: string): Observable<boolean> {
    return from([username != "Adam"]);
  }

  public saveUsersList(list: List): Observable<boolean> {
    return from([true]).pipe(delay(1000));
  }
}
