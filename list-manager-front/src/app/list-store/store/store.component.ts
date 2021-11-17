import { Component } from "@angular/core";
import { ListManger } from "../../model/list-manager.model";
import { List } from "../../model/list.model";
import { ListRepository } from "../../model/list.repository";
import { Router } from "@angular/router";

@Component({
  selector: "store",
  templateUrl: "store.component.html",
  styleUrls: ["store.component.css"]
})
export class StoreComponent {
  public selectedCategory: string | null = null;
  public listsPerPage: number = 4;
  public selectedPage: number = 1;
  public listName: string = "";
  public requestingLists: boolean = false;

  public constructor(
    private repository: ListRepository,
    private listManager: ListManger,
    private router: Router
  ) {
    repository.setUpData();
    // this.refreshLists();
  }

  public get lists(): List[] {
    // let pageIndex = (this.selectedPage - 1) * this.listsPerPage;
    // return this.repository.getLists(this.selectedCategory)
    //   .slice(pageIndex, pageIndex + this.listsPerPage);
    return this.repository.getRawLists();
  }

  public get categories(): string[] {
    return this.repository.getCategories().map(c => c.name || "");
  }

  public changeCategory(newCategory?: string) {
    this.selectedCategory = newCategory ?? null;
    this.selectedPage = 1;
    // this.repository.requestLists(this.listName, this.selectedCategory ?? "", "id", this.selectedPage, this.listsPerPage);
    this.refreshLists();
  }

  public changePage(newPage: number) {
    this.selectedPage = newPage;
    // this.repository.requestLists(this.listName, this.selectedCategory ?? "", "id", this.selectedPage, this.listsPerPage);
    this.refreshLists();
  }

  public changePageSize(newSize: number) {
    this.listsPerPage = Number(newSize);
    this.selectedPage = 1;
    // this.repository.requestLists(this.listName, this.selectedCategory ?? "", "id", this.selectedPage, this.listsPerPage);
    this.refreshLists();
  }

  public refreshLists() {
    console.log("> refreshLists");
    this.requestingLists = true;
    this.repository.requestLists(this.listName, this.selectedCategory ?? "", "id", this.selectedPage - 1, this.listsPerPage)
      .subscribe(success => {
        this.requestingLists = false;
        console.log(`Lists requested, success: ${success}`);
        console.log(this.repository.getRawLists());
      })
  }

  public get pageCount(): number {
    // let pcount = Math.ceil(this.repository
    //   .getLists(this.selectedCategory).length / this.listsPerPage);
    // console.log("PCount: ", pcount);
    let pcount = this.repository.totalPageCount;
    console.log("totalPageCount: ", pcount);
    return pcount;
  }

  public get pageNumbers(): number[] {
    // return Array(Math.ceil(this.repository
    //   .getLists(this.selectedCategory).length / this.listsPerPage))
    //   .fill(0).map((x, i) => i + 1);
    return Array(this.pageCount)
      .fill(0).map((x, i) => i + 1);
  }

  public goToLogIn() {
    // console.log("Trying go to " + "user/main");
    this.router.navigate(["user/main"]);
  }

  public setListToListManager(list: List) {
    this.listManager.setList(list);
    this.listManager.Saved = true;
    // console.log("Trying go to " + "list-store/list-viewer");
    this.router.navigate(["list-store/list-viewer"]);
  }

  public addNewList() {
    let newList = new List();
    this.listManager.setList(newList);
    this.listManager.Saved = false;
    this.router.navigate(["list-store/list-edit"]);
  }
}
