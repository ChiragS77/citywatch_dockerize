export interface PageResponse<T> {
  content: T[];
  last: boolean;
  number: number;   // current page
  size: number;
  totalPages: number;
  totalElements: number;
}