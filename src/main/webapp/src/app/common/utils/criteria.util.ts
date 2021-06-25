import {BaseCriteria, Direction, SearchPageDTO, SortDTO} from '../api/api-models';
import {DEFAULT_PAGE_SIZE} from '../const/const';

export class CriteriaBuilder {

  static init<T extends BaseCriteria>(sortBy: string = 'id', props?: any): T {
    return {
      ...props,
      searchPage: CriteriaBuilder.initSearchPage(sortBy)
    };
  }

  private static initSearchPage(sortBy?: string): SearchPageDTO {
    return {
      sorting: CriteriaBuilder.initSorting(sortBy),
      pageNumber: 0,
      pageSize: DEFAULT_PAGE_SIZE
    };
  }

  private static initSorting(sortBy?: string): SortDTO[] {
    if (!sortBy) {
      return [];
    }
    return [{
      direction: Direction.ASC,
      property: sortBy
    }];
  }

}

export class DirectionMapper {

  static map(direction: string): Direction {
    return direction === 'desc' ?
      Direction.DESC :
      Direction.ASC;
  }

}
