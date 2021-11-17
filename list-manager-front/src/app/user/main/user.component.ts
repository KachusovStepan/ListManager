import { Component } from "@angular/core";
import { Location } from "@angular/common";
import { Router } from "@angular/router";
import { AuthService } from "src/app/model/auth.service";
import { IUser } from "src/app/model/user";

@Component({
  templateUrl: "user.component.html"
})
export class UserComponent {
  public user: IUser | null = null;
  public constructor(
      private router: Router,
      private location: Location,
      private auth: AuthService) {
    console.log("UserComponent initialized");
    this.auth.getUser().subscribe(data => {
      if (!data) {
        data = {
          id: 0,
          username: "Unknown",
          email: "Unknown",
          lists: []
        }
      }
      this.user = data;
    });
  }
}

