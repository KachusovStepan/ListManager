import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { FormsModule } from "@angular/forms";
import { ModelModule } from "../model/model.module";
import { RouterModule } from "@angular/router";
import { NavigationComponent } from "./navigation/navigation.component";


@NgModule({
  imports: [RouterModule, ModelModule, BrowserModule, FormsModule],
  declarations: [NavigationComponent],
  exports: [NavigationComponent]
})
export class GeneralModule { }
