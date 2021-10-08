import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { List } from "./list.model";
import { map } from "rxjs/operators";
import { HttpHeaders } from "@angular/common/http";
import { ItemStatus } from "./itemstatus.model";
import { IUser } from "./user";
import { Category } from "./category.model";

const PROTOCOL = "http";
const PORT = 8080;

@Injectable({
  providedIn: 'root',
})
export class RestDataSource {
  public baseUrl: string = "";
  public auth_token: string | null = null;
  public user: IUser | null = null;

  public constructor(private http: HttpClient) {
    console.log("RestDataSource INIT");
      this.baseUrl = `${PROTOCOL}://${location.hostname}:${PORT}/`;
  }

  public getLists(): Observable<List[]> {
    // return this.http.get<List[]>(
    //   this.baseUrl + "api/lists", this.getOptions());
    return this.http.get<List[]>(
      this.baseUrl + "api/user/lists", this.getOptions());
  }

  public getItemStatus(): Observable<ItemStatus[]> {
    return this.http.get<ItemStatus[]>(
      this.baseUrl + "api/itemstatuses", this.getOptions());
  }

  public getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(
      this.baseUrl + "api/categories", this.getOptions());
  }

  public getUser(): Observable<IUser> {
    return this.http.get<IUser>(
      this.baseUrl + "api/user", this.getOptions());
  }

  public authenticate(user: string, pass: string): Observable<boolean> {
    return this.http.post<any>(this.baseUrl + "api/login", {
      username: user, password: pass
    }).pipe(map(response => {
      console.log("REST DATASOURCE: repsonse: ");
      console.log(response);
      // console.log(response.status);
      this.auth_token = response.access_token ? response.access_token : null;
      console.log("auth_token: " +  this.auth_token);
      return this.auth_token != null;
    }));
  }

  public register(username: string, pass: string): Observable<boolean> {
    let user = {
      id: 1034,
      username: username,
      password: pass,
      email: "",
      lists: [],
      roles: [],
    }
    return this.http.post<IUser>(this.baseUrl + "api/register", user).pipe(map(response => {
      console.log("REST DATASOURCE: repsonse: ");
      console.log(response);
      this.user = response;
      return response.username !== undefined;
    }));
  }

  private getOptions() {
    return {
      headers: new HttpHeaders({
        "Authorization": `Bearer ${this.auth_token}`
      })
    }
  }
}
