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

  static isAfterDate(isAfterDate: Date, baseDate: Date): boolean {
    return DateTimeUtils.convertDateForCompare(isAfterDate) > DateTimeUtils.convertDateForCompare(baseDate);
  }

  static isAfterOrEqualsDate(isAfterOrEqualsDate: Date, baseDate: Date): boolean {
    return DateTimeUtils.convertDateForCompare(isAfterOrEqualsDate) >= DateTimeUtils.convertDateForCompare(baseDate);
  }

  static isBeforeDate(isBeforeDate: Date, baseDate: Date): boolean {
    return DateTimeUtils.convertDateForCompare(isBeforeDate) < DateTimeUtils.convertDateForCompare(baseDate);
  }

  static isBeforeOrEqualsDate(isBeforeOrEqualsDate: Date, baseDate: Date): boolean {
    return DateTimeUtils.convertDateForCompare(isBeforeOrEqualsDate) <= DateTimeUtils.convertDateForCompare(baseDate);
  }

  static isEqualsDate(date1: Date, date2: Date): boolean {
    return DateTimeUtils.convertDateForCompare(date1) == DateTimeUtils.convertDateForCompare(date2);
  }

  private static convertDateForCompare(date: Date): string {
    const dateStr: string = DateTimeUtils.dateToString(date);
    const dateSplit: string[] = dateStr.split(DateTimeUtils.DATE_SEPARATOR);
    return dateSplit[2] + '.' + dateSplit[1] + '.' + dateSplit[0];
  }

}
