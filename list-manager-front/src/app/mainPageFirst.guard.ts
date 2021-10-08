import { Injectable } from "@angular/core";
import {
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router
} from "@angular/router";
import { MainPageComponent } from "./main-page/main-page.component";

@Injectable()
export class MainPageFirstGuard {
  private firstNavigation = true;
  public constructor(private router: Router) {

  }

  public canActivate(route: ActivatedRouteSnapshot,
      state: RouterStateSnapshot): boolean {
    if (this.firstNavigation) {
      this.firstNavigation = false;
      if (route.component != MainPageComponent) {
        // console.log("MainPageFirstGuard: returning to '/'");
        this.router.navigateByUrl("/");
        return false;
      }
    }

    return true;
  }
}
