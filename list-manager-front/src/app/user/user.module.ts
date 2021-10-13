import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";
import { AuthComponent } from "./auth/auth.component";
// import { ModelModule } from "../model/model.module";
import { UserComponent } from "./main/user.component";
import { AuthGuard } from "./auth.guard";
import { RegisterComponent } from "./register/register.component";
// import { GeneralModule } from "../general/general.module";

let routing = RouterModule.forChild([
  { path: "auth", component: AuthComponent },
  { path: "register", component: RegisterComponent },
  { path: "main", component: UserComponent,
    canActivate: [AuthGuard] },
  { path: "**", redirectTo: "auth" }
 ]);
@NgModule({
  imports: [
    CommonModule, FormsModule, routing,
    // ModelModule
  ],
  providers: [AuthGuard],
  declarations: [AuthComponent, UserComponent, RegisterComponent]
})
export class UserModule { }
