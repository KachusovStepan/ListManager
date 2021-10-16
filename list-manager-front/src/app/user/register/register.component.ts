import { Location } from "@angular/common";
import { Component } from "@angular/core";
import { NgForm } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "src/app/model/auth.service";

@Component({
  templateUrl: "register.component.html",
  styleUrls: ["register.component.css"]
})
export class RegisterComponent {
  public username: string = "";
  public password: string = "";
  public errorMessage: string | null = null;

  public constructor(
      private router: Router,
      private location: Location,
      private auth: AuthService,
    ) {
    console.log("RegisterComponent initialized");

  }

  public register(form: NgForm) {
    if (form.valid) {
      this.auth.register(this.username, this.password)
        .subscribe(response => {
          console.log("Response: ");
          console.log(response);
          if (response) {
            this.router.navigateByUrl("/user/main");
          } else {
            this.errorMessage = "Registration Failed (Maybe Name already exists)";
          }
        })
    } else {
      this.errorMessage = "Form Data Invalid";
    }
  }

  // public goBack() {
  //   this.location.back();
  // }
}
