import { Injectable } from "@angular/core";
import { Observable, from } from "rxjs";
import { catchError } from "rxjs/operators";
import { RestDataSource } from "./rest.datasource";
import { Role } from "./role.model";
import { IPostUser, IUser } from "./user";


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

  public get isAdmin(): boolean {
    // console.log(this.datasource.auth_token);
    if (this.datasource.user && this.datasource.user.roles) {
      return this.datasource.user.roles.indexOf(3) != -1;
    }
    return false; // FIXME: check in another way
  }

  public clear() {
    this.datasource.auth_token = null;
  }

  public register(username: string, password: string): Observable<boolean> {
    return this.datasource.register(username, password);
  }

  public getUser(): Observable<IUser | null> {
    return this.datasource.getUser().pipe(catchError(err => from([null])));
  }

  public updateUser(user: IPostUser): Observable<IPostUser | null> {
    return this.datasource.updateUser(user).pipe(catchError(err => from([null])));
  }

  public getRoles(): Observable<Role[] | null> {
    return this.datasource.getRoles().pipe(catchError(err => from([null])));
  }
}
