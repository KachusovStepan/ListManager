import { List } from "./list.model";

export interface IUser {
  id: number;
  username: string;
  email?: string;
  roles?: number[];
  lists?: number[];
}

export interface IPostUser {
  id?: number;
  username?: string;
  password?: string;
  email?: string;
  roles?: number[];
  lists?: number[];
}

