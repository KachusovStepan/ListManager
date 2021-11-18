import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { AuthService } from "../model/auth.service";



@Component({
  selector: "main-page",
  templateUrl: "main-page.component.html",
  styleUrls: ["main-page.component.css"]
})
export class MainPageComponent {

  public constructor(
    private router: Router,
    private auth: AuthService
  ) {}

  public get isAuthenticated(): boolean {
    // console.log("Authenticated " + this.auth.authenticated);
    return this.auth.authenticated;
  }

  public get isAdmin(): boolean {
    // console.log("Authenticated " + this.auth.authenticated);
    return this.auth.isAdmin;
  }

  public get linksToDisable(): string[] {
    return this.isAuthenticated ? [] : ["Your Lists"];
  }

  public logOut() {
    console.log("log out");
    this.auth.clear();
    this.router.navigate(["/"]);
  }

  public goToLogIn() {
    // console.log("Trying go to " + "user/main");
    this.router.navigate(["user/main"]);
  }
}
