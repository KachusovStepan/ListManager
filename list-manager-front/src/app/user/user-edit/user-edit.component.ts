import { Component } from "@angular/core";
import { Location } from '@angular/common';
import { Observable, forkJoin, from } from "rxjs";
import { AuthService } from "src/app/model/auth.service";
import { IPostUser } from "src/app/model/user";
import { NgForm } from "@angular/forms";

@Component({
  selector: "user-edit",
  templateUrl: "user-edit.component.html"
})
export class UserEditComponent {
  public user: IPostUser = {};
  public constructor(
    private auth: AuthService,
    private location: Location) {
      this.requestAll().subscribe(data => {
        this.user = {
          id: data.id,
          email: data.email ?? null,
          password: ""
        }
      });
  }

  public requestAll(): Observable<any> {
    let tasks: Observable<any>[] = [];
    tasks.push(this.auth.getUser()); // 0
    let sub = forkJoin(tasks);
    return sub;
  }



  public Saving: boolean = false;

  public errorMessage: string | null = null;

  public get User(): IPostUser {
    return this.user;
  }


  public goBack() {
    this.location.back();
  }

  public saveUser() {
    if (!this.user) {
      this.errorMessage = "user is not set";
      return;
    }
    this.Saving = true;
    this.auth.updateUser(this.user).subscribe(res => {
      this.user = res ?? {};
      this.Saving = false;
      if (res) {
        this.location.back();
      }
    });
  }
}
