import { Category } from "./category.model";
import { ListItem } from "./listitem.model";

export class List {
  public constructor(
    public id?: number,
    public name?: string,
    public category: Category = new Category(),
    public items: ListItem[] = []
  ) {}
}

export class ListToGetDto {
  public constructor(
    public id?: number,
    public name?: string,
    public category?: number,
    public items: ListItem[] = []
  ) {}

  public setUpFromList(list: List) {
    this.id = list.id;
    this.name = list.name;
    this.items = list.items;
  }
}
