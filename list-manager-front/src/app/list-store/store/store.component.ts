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
  public listsPerPage = 4;
  public selectedPage = 1;

  public constructor(
    private repository: ListRepository,
    private listManager: ListManger,
    private router: Router
  ) {
    repository.setUpData();
  }

  public get lists(): List[] {
    let pageIndex = (this.selectedPage - 1) * this.listsPerPage;
    return this.repository.getLists(this.selectedCategory)
      .slice(pageIndex, pageIndex + this.listsPerPage);
  }

  public get categories(): string[] {
    return this.repository.getCategories().map(c => c.name || "");
  }

  public changeCategory(newCategory?: string) {
    this.selectedCategory = newCategory ?? null;
  }

  public changePage(newPage: number) {
    this.selectedPage = newPage;
  }

  public changePageSize(newSize: number) {
    this.listsPerPage = Number(newSize);
    this.changePage(1);
  }

  public get pageCount(): number {
    let pcount = Math.ceil(this.repository
      .getLists(this.selectedCategory).length / this.listsPerPage);
    // console.log("PCount: ", pcount);
    return pcount;
  }

  public get pageNumbers(): number[] {
    return Array(Math.ceil(this.repository
      .getLists(this.selectedCategory).length / this.listsPerPage))
      .fill(0).map((x, i) => i + 1);
  }

  public goToLogIn() {
    // console.log("Trying go to " + "user/main");
    this.router.navigate(["user/main"]);
  }

  public setListToListManager(list: List) {
    this.listManager.setList(list);
    // console.log("Trying go to " + "list-store/list-viewer");
    this.router.navigate(["list-store/list-viewer"]);
  }
}
