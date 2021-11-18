import { Component } from "@angular/core";
import { Location } from "@angular/common";
import { Router } from "@angular/router";
import { AuthService } from "src/app/model/auth.service";
import { IUser } from "src/app/model/user";
import { ListRepository } from "src/app/model/list.repository";
import { AdminRepository } from "src/app/model/admin.repository";
import { Category } from "src/app/model/category.model";
import { Role } from "src/app/model/role.model";
import { List } from "src/app/model/list.model";

@Component({
  templateUrl: "admin.component.html"
})
export class AdminComponent {
  public user: IUser | null = null;

  public selectedCategory: string | null = null;
  public listsPerPage: number = 4;
  public usersPerPage: number = 4;
  public selectedListPage: number = 1;
  public selectedUserPage: number = 1;
  public listName: string = "";
  public requestingLists: boolean = false;
  public usersLists: boolean = false;

  public requestingData: boolean = false;

  public searchUserName: string = "";
  public selectedUser: IUser | null = null;
  public selectedRole: Role | null = null;
  public selectedRoleId: number = 0;

  public selectedList: List | null = null;

  public constructor(
      private router: Router,
      private location: Location,
      private repository: AdminRepository,
      private auth: AuthService) {
    console.log("AdminComponent initialized");
    this.auth.getUser().subscribe(data => {
      if (!data) {
        data = {
          id: 0,
          username: "Unknown",
          roles: [],
          email: "Unknown",
          lists: []
        }
      }
      this.user = data;
    });
    this.refreshUsers();
  }

  public get Roles() {
    return this.repository.getRoles();
  }

  // BR: User
  public get Users() {
    return this.repository.getUsers();
  }
  public changeUserPage(newPage: number) {
    this.selectedUserPage = newPage;
    this.refreshUsers();
  }

  public changeUserPageSize(newSize: number) {
    this.usersPerPage = Number(newSize);
    this.selectedUserPage = 1;
    this.refreshUsers();
  }

  public changeRole(role?: Role) {
    this.selectedRole = role ?? null;
    this.selectedUserPage = 1;
    this.refreshUsers();
  }

  public changeUser(user: IUser) {
    this.selectedUser = user ?? null;
    this.selectedListPage = 1;
    this.refreshLists();
  }

  public refreshUsers() {
    this.requestingData = true;
    this.repository.requestUsers(
        this.selectedRole?.id, "id",
        this.selectedUserPage - 1, this.usersPerPage)
      .subscribe(success => {
        this.requestingData = false;
        console.log(`Users requestsed, success: ${success}`);
      });
  }

  public get userPageCount(): number {
    return this.repository.userTotalPageCount;
  }

  public get userPageNumbers(): number[] {
    return Array(this.userPageCount)
      .fill(0).map((x, i) => i + 1);
  }

  // BR: Lists

  public get Lists() {
    return this.repository.getLists();
  }
  public changeListPage(newPage: number) {
    this.selectedListPage = newPage;
    this.refreshLists();
  }

  public changeListPageSize(newSize: number) {
    this.listsPerPage = Number(newSize);
    this.selectedListPage = 1;
    this.refreshLists();
  }

  public get listPageCount(): number {
    return this.repository.listTotalPageCount;
  }

  public get listPageNumbers(): number[] {
    return Array(this.listPageCount)
      .fill(0).map((x, i) => i + 1);
  }

  public refreshLists() {
    if (this.selectedUser === null) {
      return;
    }
    this.requestingData = true;
    this.repository.requestLists(
        this.selectedUser?.id, "", this.selectedCategory ?? undefined, "id",
        this.selectedListPage - 1, this.listsPerPage)
      .subscribe(success => {
        this.requestingData = false;
        console.log(`Lists requestsed, success: ${success}`);
      });
  }

  // BR: Lists
  public selectList(list: List) {
    this.selectedList = list;
  }

}

