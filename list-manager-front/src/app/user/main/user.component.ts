import { Component } from "@angular/core";
import { Observable, forkJoin, from } from "rxjs";
import { Location } from "@angular/common";
import { Router } from "@angular/router";
import { AuthService } from "src/app/model/auth.service";
import { IUser } from "src/app/model/user";
import { Role } from "src/app/model/role.model";

@Component({
  templateUrl: "user.component.html",
  styleUrls: ["./user.component.css"]
})
export class UserComponent {
  public user: IUser | null = null;
  public roles: Role[] = [];
  public idToRole: {[key: number]: Role} = {};
  public constructor(
      private router: Router,
      private location: Location,
      private auth: AuthService) {
    console.log("UserComponent initialized");
    this.requestAll().subscribe(data => {
      console.log(data);
      if (!data[0]) {
        data[0] = {
          id: 0,
          username: "Unknown",
          email: "Unknown",
          lists: []
        }
      }
      this.user = data[0];
      this.roles = data[1];
      for (let role of this.roles) {
        if (role.id) {
          this.idToRole[role.id] = role;
        }
      }
    });
  }

  public requestAll(): Observable<any> {
    let tasks: Observable<any>[] = [];
    tasks.push(this.auth.getUser()); // 0
    tasks.push(this.auth.getRoles()); // 1
    let sub = forkJoin(tasks);
    return sub;
  }

  public get UserRoles() {
    return this.user?.roles ?? [];
  }

  public RoleTitleById(id: number) {
    return this.idToRole[id].name;
  }

  // public get Title() {
  //   let title = "";
  //   if (!this.user || !this.user.roles) {
  //     return "No title";
  //   }
  // }
}

