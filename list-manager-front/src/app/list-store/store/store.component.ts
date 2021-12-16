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
  }

  public get lists(): List[] {
    return this.repository.getRawLists();
  }

  public get categories(): string[] {
    return this.repository.getCategories().map(c => c.name || "");
  }

  public changeCategory(newCategory?: string) {
    this.selectedCategory = newCategory ?? null;
    this.selectedPage = 1;
    this.refreshLists();
  }

  public changePage(newPage: number) {
    this.selectedPage = newPage;
    this.refreshLists();
  }

  public changePageSize(newSize: number) {
    this.listsPerPage = Number(newSize);
    this.selectedPage = 1;
    this.refreshLists();
  }

  public refreshLists() {
    // console.log("> refreshLists");
    this.requestingLists = true;
    this.repository.requestLists(this.listName, this.selectedCategory ?? "", "id", this.selectedPage - 1, this.listsPerPage)
      .subscribe(success => {
        this.requestingLists = false;
        // console.log(`Lists requested, success: ${success}`);
        // console.log(this.repository.getRawLists());
      })
  }

  public get pageCount(): number {
    let pcount = this.repository.totalPageCount;
    // console.log("totalPageCount: ", pcount);
    return pcount;
  }

  public get pageNumbers(): number[] {
    return Array(this.pageCount)
      .fill(0).map((x, i) => i + 1);
  }

  public goToLogIn() {
    this.router.navigate(["user/main"]);
  }

  public setListToListManager(list: List) {
    this.listManager.setList(list);
    this.listManager.Saved = true;
    this.router.navigate(["list-store/list-viewer"]);
  }

  public addNewList() {
    let newList = new List();
    this.listManager.setList(newList);
    this.listManager.Saved = false;
    this.router.navigate(["list-store/list-edit"]);
  }

  public deleteList(id: number) {
    if (confirm("Do you really want to delete this list?")) {
      this.repository.deleteList(id).subscribe(res => {
        if (res) {
          alert("List successfully deleted");
        } else {
          alert("Operation failed");
        }
      })
    }
  }
}
