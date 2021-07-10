export class DateTimeUtils {

  private static readonly DATE_SEPARATOR: string = '.';
  private static readonly TIME_SEPARATOR: string = ':';
  private static readonly DATE_TIME_SEPARATOR: string = ' ';

  static stringToDate(dateString: string): Date {
    if (!dateString) {
      return null;
    }

    const dateSplit: string[] = dateString.split(DateTimeUtils.DATE_SEPARATOR);
    if (dateSplit.length != 3) {
      return null;
    }

    try {
      const day: number = +dateSplit[0];
      const month: number = +dateSplit[1] - 1;
      const year: number = +dateSplit[2];
      return new Date(year, month, day);
    } catch {
      return null;
    }
  }

  static dateTimeToString(date: Date): string {
    return DateTimeUtils.dateToString(date) +
      DateTimeUtils.DATE_TIME_SEPARATOR +
      DateTimeUtils.timeToString(date);
  }

  static dateToString(date: Date): string {
    if (!date) {
      return '';
    }
    const day: number = date.getDate();
    const month: number = date.getMonth() + 1;
    const year: number = date.getFullYear();
    return DateTimeUtils.appendZeroIfNeed(day) +
      DateTimeUtils.DATE_SEPARATOR +
      DateTimeUtils.appendZeroIfNeed(month) +
      DateTimeUtils.DATE_SEPARATOR +
      year;
  }

  static timeToString(date: Date): string {
    if (!date) {
      return '';
    }
    const hours: number = date.getHours();
    const minutes: number = date.getMinutes();
    const seconds: number = date.getSeconds();
    return DateTimeUtils.appendZeroIfNeed(hours) +
      DateTimeUtils.TIME_SEPARATOR +
      DateTimeUtils.appendZeroIfNeed(minutes) +
      DateTimeUtils.TIME_SEPARATOR +
      DateTimeUtils.appendZeroIfNeed(seconds);
  }

  private static appendZeroIfNeed(val: number): string {
    if (!val) {
      return '';
    }
    const prefix: string = val < 10 ? '0' : '';
    return prefix + val;
  }

}
