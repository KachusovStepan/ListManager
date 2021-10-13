import { ItemStatus } from "./itemstatus.model";

export class ListItem{
  public constructor(
    public id?: number,
    public number: number = 0,
    public priority: number = 0,
    public description: string = "",
    public status?: ItemStatus
  ) {}
}
