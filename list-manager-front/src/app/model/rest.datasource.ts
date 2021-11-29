import { ComponentFactoryResolver, Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { from, Observable } from "rxjs";
import { List, ListToGetDto } from "./list.model";
import { map, catchError } from "rxjs/operators";
import { HttpHeaders } from "@angular/common/http";
import { ItemStatus } from "./itemstatus.model";
import { IUser } from "./user";
import { Category } from "./category.model";
import { CustomPage } from "./customPage.model";
import { flatMap } from "rxjs/internal/operators";
import { Role } from "./role.model";

const PROTOCOL = "http";
const PORT = 8080;

@Injectable({
  providedIn: 'root',
})
export class RestDataSource {
  public baseUrl: string = "";
  public auth_token: string | null = null;
  public refresh_token: string | null = null;
  public user: IUser | null = null;

  public constructor(private http: HttpClient) {
    console.log("RestDataSource INIT");
    this.baseUrl = `${PROTOCOL}://${location.hostname}:${PORT}/`;
  }

  private tokenExpired(token: string) {
    const expiry = (JSON.parse(atob(token.split('.')[1]))).exp;
    return (Math.floor((new Date).getTime() / 1000)) >= expiry;
  }

  private tokenExpiresTime(token: string) {
    return (JSON.parse(atob(token.split('.')[1]))).exp;
  }

  private tokenExpiresLessThan(token: string, sec: number) {
    let tokenExpAt = (JSON.parse(atob(token.split('.')[1]))).exp;
    console.log(`Access token exp at (ts): ${tokenExpAt}`);
    console.log(`Now : ${Math.round(Date.now() / 1000)}`);

    return (tokenExpAt - Math.round(Date.now() / 1000)) < sec;
  }

  public refreshTokens() {
    return this.http.get<any>(
      this.baseUrl + "api/token/refresh", this.getRefreshOptions()
    ).pipe(map(response => {
      console.log("REST DATASOURCE: refresh access_token: ");
      console.log(response);
      this.auth_token = response.access_token ? response.access_token : null;
      this.refresh_token = response.refresh_token ? response.refresh_token : null;
      console.log("auth_token: " + this.auth_token);
      console.log("refresh_token: " + this.refresh_token);
      if (this.auth_token) {
        console.log(`auth_token exp at: ${new Date(this.tokenExpiresTime(this.auth_token))}`)
      }
      if (this.refresh_token) {
        console.log(`refresh_token exp at: ${new Date(this.tokenExpiresTime(this.refresh_token))}`)
      }
      return this.auth_token != null;
    }));
  }

  public getLists(): Observable<List[]> {
    let url = this.baseUrl + "api/user/lists";
    return this.refreshTokenIfNeeded().pipe(flatMap(succ => {
      return this.http.get<List[]>(url, this.getOptions());
    }));
  }

  public getListsWithParams(listName: string = "", categoryName: string = "", sortBy: string = "id", pageIndex: number = 0, pageSize: number = 8): Observable<CustomPage<ListToGetDto>> {
    let url = this.baseUrl + `api/user/lists?name=${listName}&categoryName=${categoryName}&sortBy=${sortBy}&pageIndex=${pageIndex}&pageSize=${pageSize}`;
    return this.refreshTokenIfNeeded().pipe(flatMap(succ => {
      return this.http.get<CustomPage<ListToGetDto>>(url, this.getOptions());
    }));
  }

  public getUsersWithParams(roleId: number | null = null, sortBy: string = "id", pageIndex: number = 0, pageSize: number = 8): Observable<CustomPage<IUser>> {
    let url = this.baseUrl + `api/users?`;
    if (roleId !== null) {
      url += `roleId=${roleId}&`;
    }
    url +=`sortBy=${sortBy}&pageIndex=${pageIndex}&pageSize=${pageSize}`;

    return this.refreshTokenIfNeeded().pipe(flatMap(succ => {
      return this.http.get<CustomPage<IUser>>(url, this.getOptions());
    }));
  }

  public getListsWithParamsUsingUserId(userId: number, listName: string = "", categoryName: string = "", sortBy: string = "id",
      pageIndex: number = 0, pageSize: number = 4): Observable<CustomPage<ListToGetDto>> {
    let url = this.baseUrl + `api/users/${userId}/lists?name=${listName}&categoryName=${categoryName}&sortBy=${sortBy}&pageIndex=${pageIndex}&pageSize=${pageSize}`
    return this.refreshTokenIfNeeded().pipe(flatMap(succ => {
      return this.http.get<CustomPage<ListToGetDto>>(url, this.getOptions());
    }));
  }

  public getItemStatus(): Observable<ItemStatus[]> {
    let url = this.baseUrl + "api/itemstatuses";
    return this.refreshTokenIfNeeded().pipe(flatMap(succ => {
      return this.http.get<ItemStatus[]>(url, this.getOptions());
    }));
  }

  public getCategories(): Observable<Category[]> {
    let url = this.baseUrl + "api/categories";
    return this.refreshTokenIfNeeded().pipe(flatMap(succ => {
      return this.http.get<Category[]>(url, this.getOptions());
    }));
  }

  public getRoles(): Observable<Role[]> {
    let url = this.baseUrl + "api/roles";
    return this.refreshTokenIfNeeded().pipe(flatMap(succ => {
      return this.http.get<Role[]>(url, this.getOptions());
    }));
  }

  public getUser(): Observable<IUser> {
    let url = this.baseUrl + "api/user";
    return this.refreshTokenIfNeeded().pipe(flatMap(succ => {
      return this.http.get<IUser>(
        url, this.getOptions());
    }));
  }

  public saveUsersList(list: List): Observable<List | null> {
    console.log("REST DATASOURCE: saving list");
    let url = this.baseUrl + "api/user/lists";
    return this.refreshTokenIfNeeded().pipe(flatMap(succ => {
      return this.http.post<List>(url, list, this.getOptions())
        .pipe(map(response => {
          console.log("REST DATASOURCE: response");
          console.log(response);
          return response;
        }));
    }));
  }

  public deleteUsersList(id: number): Observable<boolean> {
    let url = this.baseUrl + `api/user/lists/${id}`;
    return this.refreshTokenIfNeeded().pipe(flatMap(succ => {
      return this.http.delete<void>(url, this.getOptions())
      .pipe(map(res => true))
      .pipe(catchError(err => from([false])));
    }));
  }


  public deleteListById(listId: number): Observable<boolean> {
    let url = this.baseUrl + `api/lists/${listId}`;
    return this.refreshTokenIfNeeded().pipe(flatMap(succ => {
      return this.http.delete<void>(url, this.getOptions())
      .pipe(map(res => true))
      .pipe(catchError(err => from([false])));
    }));
  }

  public authenticate(user: string, pass: string): Observable<boolean> {
    return this.http.post<any>(this.baseUrl + "api/login", {
      username: user, password: pass
    }).pipe(map(response => {
      console.log("REST DATASOURCE: repsonse: ");
      console.log(response);
      // console.log(response.status);
      // this.auth_token = null;
      this.auth_token = response.access_token ? response.access_token : null;
      this.refresh_token = response.refresh_token ? response.refresh_token : null;
      console.log("auth_token: " + this.auth_token);
      console.log("refresh_token: " + this.refresh_token);
      if (this.auth_token) {
        console.log(`auth_token exp at: ${new Date(this.tokenExpiresTime(this.auth_token))}`)
      }
      if (this.refresh_token) {
        console.log(`refresh_token exp at: ${new Date(this.tokenExpiresTime(this.refresh_token))}`)
      }
      // NEW:
      if (this.auth_token) {
        this.getUser().subscribe(user => {
          this.user = user;
        });
      }
      return this.auth_token != null;
    })).pipe(catchError(err => from([false])));
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
      return response.id !== undefined;
    })).pipe(catchError(err => from([false])));
  }


  private refreshTokenIfNeeded() {
    if (this.refresh_token && this.tokenExpiresLessThan(this.refresh_token, 5)) {
      return this.refreshTokens();
    }
    return from([true]);
  }

  private getOptions() {
    return {
      headers: new HttpHeaders({
        "Authorization": `Bearer ${this.auth_token}`
      })
    }
  }

  private getRefreshOptions() {
    return {
      headers: new HttpHeaders({
        "Authorization": `Bearer ${this.refresh_token}`
      })
    }
  }
}
