import { Location } from "@angular/common";
import {Component} from "@angular/core";
import { NgForm } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../../model/auth.service";

@Component({
  templateUrl: "auth.component.html",
  styleUrls: ["auth.component.css"]
})
export class AuthComponent {
  public username: string = "";
  public password: string = "";
  public errorMessage: string | null = null;

  public constructor(
      private router: Router,
      private location: Location,
      private auth: AuthService,
    ) {
    // console.log("AuthComponent initialized");

  }

  public authenticate(form: NgForm) {
    if (form.valid) {
      this.auth.authenticate(this.username, this.password)
        .subscribe(response => {
          // console.log("Response: ");
          // console.log(response);
          if (response) {
            this.router.navigateByUrl("/user/main");
          } else {
            this.errorMessage = "Authentication Failed";
          }
        })
    } else {
      this.errorMessage = "Form Data Invalid";
    }
  }

  public goBack() {
    this.location.back();
  }
}
