import { List } from "./list.model";

export interface IUser {
  id: number;
  username: string;
  email?: string;
  lists?: number[];
}
