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

@Injectable()
export class ListRepository {
  private lists: List[] = [];
  private categories: Category[] = [];
  private allCategories: Category[] = [];
  private itemStatuses: ItemStatus[] = [];
  private user: IUser | null = null;
  public totalCount: number = 0;
  public totalPageCount: number = 0;
  public pageIndex: number = 0;
  public pageSize: number = 0;

  constructor(private dataSource: RestDataSource) {
    console.log("ListRepository INIT");
    // this.requestAll().subscribe(data => {
    //   this.lists = data[0];
    //   this.categories = data[0].map((l: List) => l.category)
    //     .filter((c: Category, index: number, array: Category[]) => array.indexOf(c) == index)
    //     .filter(notEmpty)
    //     .sort();
    //   this.itemStatuses = data[1];
    //   console.log("Get data from datasource");
    //   console.log(this.lists);
    //   console.log(this.categories);
    // });
    this.setUpData();
  }

  public setUpData(): void {
    this.requestAll().subscribe(data => {
      this.user = data[0];
      this.itemStatuses = data[1];
      this.allCategories = data[2];

      // LOG
      console.log("Get data from datasource:");
      console.log("User");
      console.log(this.user);
      console.log("allCategories");
      console.log(this.allCategories);
      console.log("ItemStatuses");
      console.log(this.itemStatuses);


      console.log(`Requesting lists`);
      this.requestLists().subscribe(
        succ => console.log(`requestsed lists: success: ${succ}`)
      );
    });
  }

  public requestAll(): Observable<any> {
    let tasks: Observable<any>[] = [];
    tasks.push(this.dataSource.getUser()); // 0
    tasks.push(this.dataSource.getItemStatus()); // 1
    tasks.push(this.dataSource.getCategories()); // 2
    let sub = forkJoin(tasks);
    return sub;
  }

  public requestLists(
      listName: string = "", categoryName: string = "", sortBy: string = "id",
      pageIndex: number = 0, pageSize: number = 4): Observable<boolean> {
    if (this.user === null) {
      console.log("$> requestLists: user not set");
      return from([false]);
    }
    return this.dataSource.getListsWithParams(listName, categoryName, sortBy, pageIndex, pageSize).pipe(
      map(page => {
        this.totalCount = page.totalCount;
        this.totalPageCount = page.totalPageCount;
        this.pageIndex = page.pageIndex;
        this.pageSize = page.pageSize;
        this.lists = [];
        for (let list of page.data) {
          let newList: List = ListToGetDto2List(list);
          newList.category = this.allCategories.find(l => l.id == list.category) ?? new Category();
          this.lists.push(newList);
        }
        console.log(`ListRepository: got list data ${this.lists.length}`);
        console.log(this.lists.length);
        return true;
      })
    )
  }

  public getRawLists() {
    return this.lists;
  }

  public getLists(category: string | null = null): List[] {
    return this.lists
      .filter(l => category == null || category == l.category?.name);
  }

  public getList(id: number): List | undefined {
    return this.lists.find(l => l.id == id);
  }


  public getCategories(): Category[] {
    return this.allCategories;
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
