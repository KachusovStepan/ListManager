import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";
import { AuthComponent } from "./auth/auth.component";
import { AdminRepository} from "../model/admin.repository";
import { UserComponent } from "./main/user.component";
import { AdminComponent } from "./admin/admin.component";
import { AuthGuard } from "./auth.guard";
import { RegisterComponent } from "./register/register.component";

let routing = RouterModule.forChild([
  { path: "auth", component: AuthComponent },
  { path: "register", component: RegisterComponent },
  { path: "main", component: UserComponent,
    canActivate: [AuthGuard] },
  { path: "admin", component: AdminComponent,
    canActivate: [AuthGuard] },
  { path: "**", redirectTo: "auth" }
 ]);
@NgModule({
  imports: [
    CommonModule, FormsModule, routing,
    // ModelModule
  ],
  providers: [AuthGuard, AdminRepository],
  declarations: [AuthComponent, UserComponent, AdminComponent, RegisterComponent]
})
export class UserModule { }
