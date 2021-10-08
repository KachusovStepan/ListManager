import { Category } from "./category.model";
import { ListItem } from "./listitem.model";

export class List {
  public constructor(
    public id?: number,
    public name?: string,
    public category?: Category,
    public items?: ListItem[]
  ) {}
}
