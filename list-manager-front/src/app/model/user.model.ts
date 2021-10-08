import { Role } from "./role.model";

export class User {
  public constructor(
    public id?: number,
    public username?: string,
    public email?: string,
    public roles?: Role[]
  ) {}
}
