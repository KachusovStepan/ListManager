import { Injectable } from "@angular/core";
import { List, ListToGetDto } from "./list.model";
import { Category } from "./category.model";
import { ListItem } from "./listitem.model";
import { ItemStatus } from "./itemstatus.model";

import { Observable, from } from "rxjs";
import { delay } from 'rxjs/internal/operators';

import { IUser } from "./user";
import { CustomPage } from "./customPage.model";
import { Role } from "./role.model";

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
  private roles: Role[] = [
    new Role(1, "ROLE_USER"),
    new Role(2, "ROLE_MANAGER"),
    new Role(3, "ROLE_ADMIN"),
  ];
  private listItems: ListItem[] = [
    new ListItem(1, 1, 1, "Buy Flowers", this.itemsStatuses[2]),
    new ListItem(2, 2, 1, "Find Shoes", this.itemsStatuses[1]),
    new ListItem(3, 1, 1, "Call Joe", this.itemsStatuses[0]),
  ];
  private lists: List[] = [
    new List(1, "Adam's List", this.categories[0], [this.listItems[0], this.listItems[1], this.listItems[2]]),
    new List(2, "My List 1", this.categories[1], [this.listItems[0], this.listItems[1]]),
    new List(3, "My List 2", this.categories[0], [this.listItems[0], this.listItems[2]]),
    new List(4, "My List 3", this.categories[2], [this.listItems[0], this.listItems[1]]),
    new List(5, "My List 4", this.categories[1], [this.listItems[0], this.listItems[2]]),
    new List(6, "My List 5", this.categories[2], [this.listItems[0], this.listItems[1]]),
  ];
  private listToGetDtos: ListToGetDto[] = this.lists.map(l => {
    let ldto = new ListToGetDto();
    ldto.setUpFromList(l);
    return ldto;
  });

  public auth_token: string = "";
  public user: IUser = {
    id: 1,
    username: "Adam",
    email: "a@a.com",
    roles: [1, 2, 3],
    lists: this.lists.map(l => l.id ?? -1)//[this.lists[0]]
  };
  public users: IUser[] = [this.user, this.user, this.user, this.user, this.user,
    { id: 2,
      username: "Robert",
      email: "r@r.com",
      roles: [1],
      lists: this.lists.map(l => l.id ?? -1)}
  ];

  public getLists(): Observable<List[]> {
    return from([this.lists]);
  }

  public getListsWithParams(
      listName: string = "", categoryName: string = "",
      sortBy: string = "id", pageIndex: number = 0,
      pageSize: number = 8): Observable<CustomPage<ListToGetDto>> {
    let totalCount = this.listToGetDtos.length;
    let totalPageCount = Math.ceil(totalCount / pageSize);
    let start = pageSize * (pageIndex);
    let end = Math.min(totalCount, start + pageSize);
    console.log(` > getListsWithParams: (${start}, ${end})`);
    let data = this.listToGetDtos.slice(start, end);
    let page = new CustomPage<ListToGetDto>(totalCount, totalPageCount, pageIndex, pageSize, data);
    return from([page]);
  }

  public getListsWithParamsUsingUserId(userId: number, listName: string = "", categoryName: string = "", sortBy: string = "id",
    pageIndex: number = 0, pageSize: number = 4): Observable<CustomPage<ListToGetDto>> {
      return this.getListsWithParams(listName, categoryName, sortBy, pageIndex, pageSize);
  }

  public getUsersWithParams(
      roleId: number | null = null,
      sortBy: string = "id", pageIndex: number = 0,
      pageSize: number = 8): Observable<CustomPage<IUser>> {
        console.log("request users");
    let totalCount = this.users.length;
    let totalPageCount = Math.ceil(totalCount / pageSize);
    let start = pageSize * (pageIndex);
    let end = Math.min(totalCount, start + pageSize);
    console.log(` > getUsersWithParams: (${start}, ${end})`);
    let data = this.users.slice(start, end);
    let page = new CustomPage<IUser>(totalCount, totalPageCount, pageIndex, pageSize, data);
    console.log(data);
    return from([page]);
  }

  public getItemStatus(): Observable<ItemStatus[]> {
    return from([this.itemsStatuses]);
  }

  public getCategories(): Observable<ItemStatus[]> {
    return from([this.categories]);
  }

  public getRoles(): Observable<Role[]> {
    return from([this.roles]);
  }

  public getUser(): Observable<IUser> {
    return from([this.user]);
  }

  public getListById(id: number): Observable<ListToGetDto> {
    return from([this.listToGetDtos[0]]);
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

  public deleteListById(listId: number): Observable<boolean> {
    return from([true]).pipe(delay(1000));
  }
}
