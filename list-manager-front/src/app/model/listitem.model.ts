import { ItemStatus } from "./itemstatus.model";

export class ListItem{
  public constructor(
    public id?: number,
    public number?: number,
    public priority?: number,
    public description?: string,
    public status?: ItemStatus
  ) {}
}
