import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, RouterStateSnapshot, Router } from "@angular/router";
import { AuthService } from "../model/auth.service";


@Injectable()
export class AuthGuard {
  constructor(
    private router: Router,
    private auth: AuthService
  ) { }

  public canActivate(
      route: ActivatedRouteSnapshot,
      state: RouterStateSnapshot
    ): boolean
  {
    // console.log("Auth: " + state.url);
    if (!this.auth.authenticated && state.url != '/user/register') {
      this.router.navigateByUrl("/user/auth");
      return false;
    }
    return true;
  }
}
