import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { FormsModule } from "@angular/forms";
// import { ModelModule } from "../model/model.module";
import { StoreComponent } from "./store/store.component";
import { CounterDirective } from "./counter.directive";
import { RouterModule } from "@angular/router";
import { GeneralModule } from "../general/general.module";
import { ListSummaryComponent } from "./list-summary/list-summary.component";
import { ListViewerComponent } from "./list-viewer/list-viewer.component";

let routing = RouterModule.forChild([
  { path: "profile", component: ListViewerComponent },
  { path: "list-store/list-viewer", component: ListViewerComponent },
  // { path: "", redirectTo: "/list-store", pathMatch: 'full' },
  // { path: "**", redirectTo: "/list-store" },
]);


@NgModule({
  imports: [
    RouterModule, BrowserModule, FormsModule, routing,
    // ModelModule,
    GeneralModule
  ],
  declarations: [StoreComponent, CounterDirective, ListSummaryComponent, ListViewerComponent],
  exports: [StoreComponent]
})
export class ListStoreModules { }


