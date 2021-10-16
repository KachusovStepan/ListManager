import { NgModule } from "@angular/core";
import { ListRepository } from "./list.repository";
import { StaticDataSource } from "./static.datasource";
import { ListManger } from "./list-manager.model";
import { AuthService } from "./auth.service";
import { RestDataSource } from "./rest.datasource";
import { HttpClientModule } from "@angular/common/http";


@NgModule({
  imports: [HttpClientModule],
  providers: [
    ListRepository, StaticDataSource, ListManger,
    // { provide: RestDataSource, useClass: StaticDataSource},
    RestDataSource,
    AuthService,
  ],
})
export class ModelModule { }
