
export interface Paging {
  total: number,
  page: number,
  size: number
}

export interface Response<T>{
  errorMap: Object,
  pagination?: Paging,
  content: T
}
