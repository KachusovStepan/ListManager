import { Injectable } from "@angular/core";
import { Category } from "./category.model";
import { List } from "./list.model";
import { StaticDataSource } from "./static.datasource";
import { Observable, forkJoin } from "rxjs";
import { map } from "rxjs/operators";
import { ItemStatus } from "./itemstatus.model";
import { RestDataSource } from "./rest.datasource";
import { IUser } from "./user";

@Injectable()
export class ListRepository {
  private lists: List[] = [];
  private categories: Category[] = [];
  private allCategories: Category[] = [];
  private itemStatuses: ItemStatus[] = [];
  private user: IUser | null = null;

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
      this.lists = data[1];
      this.categories = this.lists.map((l: List) => l.category)
        .filter((c, index: number, array) => array.indexOf(c) == index)
        .filter(notEmpty)
        .sort();
      this.itemStatuses = data[2];
      this.allCategories = data[3];
      console.log("Get data from datasource");
      console.log(this.lists);
      console.log(this.categories);
    });
  }

  public requestAll(): Observable<any> {
    let tasks: Observable<any>[] = [];
    tasks.push(this.dataSource.getUser());
    tasks.push(this.dataSource.getLists());
    tasks.push(this.dataSource.getItemStatus());
    tasks.push(this.dataSource.getCategories());
    // let sub = forkJoin(tasks).pipe(map(data => {
    // }));
    let sub = forkJoin(tasks);

    return sub;
  }

  public getLists(category: string | null = null): List[] {
    return this.lists
      .filter(l => category == null || category == l.category?.name);
  }

  public getList(id: number): List | undefined {
    return this.lists.find(l => l.id == id);
  }

  public getCategories(): Category[] {
    return this.categories;
  }

  public getItemStatuses(): ItemStatus[] {
    return this.itemStatuses;
  }

  public getUser(): IUser | null {
    return this.user;
  }
}


function notEmpty<TValue>(value: TValue | null | undefined): value is TValue {
  return value !== null && value !== undefined;
}
