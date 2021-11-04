

export class CustomPage<T> {
  public constructor(
    public totalCount: number = 0,
    public totalPageCount: number = 0,
    public pageIndex: number = 0,
    public pageSize: number = 0,
    public data: T[]
  ) {}
}
