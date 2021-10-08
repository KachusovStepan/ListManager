import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { FormsModule } from "@angular/forms";
import { ModelModule } from "../model/model.module";
import { RouterModule } from "@angular/router";
import { GeneralModule } from "../general/general.module";
import { MainPageComponent } from "./main-page.component";

// let routing = RouterModule.forChild([
//   { path: "", redirectTo: "/main-page", pathMatch: 'full' },
//   { path: "**", redirectTo: "/main-page" },
// ]);


@NgModule({
  imports: [
    RouterModule, BrowserModule, FormsModule,
    // ModelModule,
    GeneralModule
  ],
  declarations: [MainPageComponent],
  exports: [MainPageComponent]
})
export class MainPageModule { }


