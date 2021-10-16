import { Component, Input, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { INavigationOption } from "../../common/types";
import { RestDataSource } from "../../model/rest.datasource";



@Component({
  selector: "navigation",
  templateUrl: "navigation.component.html",
  styleUrls: ["navigation.component.css"]
})
export class NavigationComponent implements OnInit {
  private _activeLink: string = "";
  private _disabledLinks: string[] = [];
  @Input()
  get activeLink(): string { return this._activeLink; }
  set activeLink(linkName: string) {
    this._activeLink = linkName;
    this.setActiveLink(linkName);
  }
  @Input()
  get disabledLinks(): string[] { return this._disabledLinks; }
  set disabledLinks(linkNames: string[]) {
    this._disabledLinks = linkNames;
    this.disableLinks(linkNames);
  }

  public constructor(
    private router: Router,
    public datasource: RestDataSource
  ) {}

  public ngOnInit() {
    this.setActiveLink(this.activeLink);
    this.disableLinks(this.disabledLinks);
    console.log("Navigation:");
    console.log(this.navigationOptions);
  }

  private navigationOptions: INavigationOption[] = [
    { title: "Home", rlink: "/main-page", isActive: true, isDisabled: false},
    { title: "Your Lists", rlink: "/list-store", isActive: false, isDisabled: false },
    { title: "Profile", rlink: "/user/main", isActive: false, isDisabled: false },
  ]


  public setActiveLink(linkName: string): void {
    this.navigationOptions.forEach(noption => {
      noption.isActive = noption.title == linkName;
    });
  }

  public disableLinks(linkNames: string[]): void {
    this.navigationOptions.forEach(noption => {
      noption.isDisabled = linkNames.includes(noption.title);
    });
  }

  public isAuthorized() {
    return this.datasource.auth_token != null;
  }

  public get NavigationOptions(): INavigationOption[] {
    return this.navigationOptions;
  }

}
