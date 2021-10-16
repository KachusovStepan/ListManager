import {  Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { RestDataSource } from "./rest.datasource";

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  public constructor(private datasource: RestDataSource) {
    console.log("AuthService INIT");
  }

  public authenticate(username: string, password: string): Observable<boolean> {
    return this.datasource.authenticate(username, password);
  }

  public get authenticated(): boolean {
    // console.log(this.datasource.auth_token);
    return this.datasource.auth_token != null;
  }

  public clear() {
    this.datasource.auth_token = null;
  }

  public register(username: string, password: string): Observable<boolean> {
    return this.datasource.register(username, password);
  }
}
