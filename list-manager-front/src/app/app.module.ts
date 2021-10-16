import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { ListStoreModules } from './list-store/list-store.module';
import { MainPageModule } from './main-page/main-page.module';
import { StoreComponent } from './list-store/store/store.component';
import { MainPageFirstGuard } from './mainPageFirst.guard';
import { MainPageComponent } from './main-page/main-page.component';
import { GeneralModule } from './general/general.module';
import { ModelModule } from './model/model.module';


const rotes: Routes = [
  { path: "list-store", component: StoreComponent,
    canActivate: [MainPageFirstGuard]
  },
  { path: "main-page", component: MainPageComponent,
    canActivate: [MainPageFirstGuard]
  },
  {
    path: "user",
    loadChildren: () => import("./user/user.module")
      .then(m => m.UserModule),
    canActivate: [MainPageFirstGuard]
  },
  { path: "**", redirectTo: "/main-page" },
];

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    ListStoreModules, MainPageModule, GeneralModule, ModelModule,
    RouterModule.forRoot(rotes),
  ],
  providers: [MainPageFirstGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
