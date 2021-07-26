import {ProjectBaseDTO, UserDTO} from '../../../../common/api/api-models';
import {DateTimeUtils} from '../../../../common/utils/date-time.utils';
import {StringUtils} from '../../../../common/utils/string.utils';

export class ProjectHelper {

  static projectHasOverlappingDates(project: ProjectBaseDTO, dateStartBase: Date, dateEndBase: Date, excludedProjectId: number): boolean {
    if (excludedProjectId && excludedProjectId === project.id) {
      return false;
    }
    const dateStart: Date = DateTimeUtils.stringToDate(project.dateStartStr);
    const dateEnd: Date = DateTimeUtils.stringToDate(project.dateEndStr);
    return !(DateTimeUtils.isBeforeDate(dateEnd, dateStartBase) || DateTimeUtils.isAfterDate(dateStart, dateEndBase));
  }

  static compareUser(u1: UserDTO, u2: UserDTO): number {
    let compare: number = StringUtils.compareString(u1.lastName, u2.lastName);
    if (compare !== 0) {
      return compare;
    }
    compare = StringUtils.compareString(u1.firstName, u2.firstName);
    if (compare !== 0) {
      return compare;
    }
    return StringUtils.compareString(u1.email, u2.email);
  }

}
