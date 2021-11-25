import { Injectable } from "@angular/core";
import { Category } from "./category.model";
import { List, ListToGetDto } from "./list.model";
import { StaticDataSource } from "./static.datasource";
import { Observable, forkJoin, from } from "rxjs";
import { map } from "rxjs/operators";
import { ItemStatus } from "./itemstatus.model";
import { RestDataSource } from "./rest.datasource";
import { IUser } from "./user";
import { CustomPage } from "./customPage.model";
import { Role } from "./role.model";

@Injectable()
export class AdminRepository {
  private lists: List[] = [];
  private categories: Category[] = [];
  private allCategories: Category[] = [];
  private itemStatuses: ItemStatus[] = [];
  private roles: Role[] = [];
  private user: IUser | null = null;
  private users: IUser[] = [];
  public publicSelectedUser: IUser | null = null;

  public listTotalCount: number = 0;
  public listTotalPageCount: number = 0;
  public listPageIndex: number = 0;
  public listPageSize: number = 0;

  public userTotalCount: number = 0;
  public userTotalPageCount: number = 0;
  public userPageIndex: number = 0;
  public userPageSize: number = 0;

  constructor(private dataSource: RestDataSource) {
    console.log("AdminRepository INIT");
    this.setUpData();
    this.requestUsers();
  }

  public setUpData(): void {
    this.requestAll().subscribe(data => {
      this.user = data[0];
      this.itemStatuses = data[1];
      this.allCategories = data[2];
      this.roles = data[3];

      console.log(`Requesting init data`);
      console.log(data);
      // this.requestLists().subscribe(
      //   succ => console.log(`requestsed: success: ${succ}`)
      // );
    });
  }

  public requestAll(): Observable<any> {
    let tasks: Observable<any>[] = [];
    tasks.push(this.dataSource.getUser()); // 0
    tasks.push(this.dataSource.getItemStatus()); // 1
    tasks.push(this.dataSource.getCategories()); // 2
    tasks.push(this.dataSource.getRoles()); // 3
    let sub = forkJoin(tasks);
    return sub;
  }

  public requestLists(
      userId: number, listName = "", categoryName: string = "", sortBy: string = "id",
      pageIndex: number = 0, pageSize: number = 4): Observable<boolean> {
    if (this.user === null) {
      console.log("$> requestLists: user not set");
      return from([false]);
    }
    return this.dataSource.getListsWithParamsUsingUserId(userId, listName, categoryName, sortBy, pageIndex, pageSize).pipe(
      map(page => {
        this.listTotalCount = page.totalCount;
        this.listTotalPageCount = page.totalPageCount;
        this.listPageIndex = page.pageIndex;
        this.listPageSize = page.pageSize;
        this.lists = [];
        for (let list of page.data) {
          let newList: List = ListToGetDto2List(list);
          newList.category = this.allCategories.find(l => l.id == list.category) ?? new Category();
          this.lists.push(newList);
        }
        return true;
      })
    )
  }

  public requestUsers(
    roleId: number | null = null, sortBy: string = "id",
    pageIndex: number = 0, pageSize: number = 4): Observable<boolean> {
  if (this.user === null) {
    console.log("$> requestUsers: user not set");
    return from([false]);
  }
  return this.dataSource.getUsersWithParams(roleId, sortBy, pageIndex, pageSize).pipe(
    map(page => {
      this.userTotalCount = page.totalCount;
      this.userTotalPageCount = page.totalPageCount;
      this.userPageIndex = page.pageIndex;
      this.userPageSize = page.pageSize;
      this.users = page.data;
      return true;
    })
  )
}

  public getLists() {
    return this.lists;
  }

  public getUsers() {
    return this.users;
  }

  public getCategories(): Category[] {
    return this.allCategories;
  }

  public getRoles(): Role[] {
    return this.roles;
  }

  public getItemStatuses(): ItemStatus[] {
    return this.itemStatuses;
  }

  public getUser(): IUser | null {
    return this.user;
  }

  public saveList(list: List): Observable<boolean> {
    return this.dataSource.saveUsersList(list).pipe(map(result => {
      if (!this.user) {
        console.log("REST DATASOURCE: user not set!!!");
        return false;
      }
      if (result === null) {
        return false;
      }
      console.log("Saved list");
      console.log(result);
      // TODO: make sure it will be displayed
      // this.lists.push(result);
      return true;
    }
  ));
  }

  public deleteList(id: number): Observable<boolean> {
      return this.dataSource.deleteUsersList(id).pipe(map(res => {
        if (res) {
          this.lists = this.lists.filter(l => l.id != id);
        }
        return res;
      }));
  }
}


function notEmpty<TValue>(value: TValue | null | undefined): value is TValue {
  return value !== null && value !== undefined;
}


function ListToGetDto2List(list: ListToGetDto) {
  let newList: List = new List();
  newList.id = list.id;
  newList.name = list.name;
  newList.items = list.items;

  return newList;
}
