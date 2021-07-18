export class StringUtils {

  static compareString(str1: string, str2: string): number {
    if (str1 > str2) {
      return 1;
    }
    if (str1 < str2) {
      return -1;
    }
    return 0;
  }

  static containIgnoreCase(checkedText: string, containingText: string): boolean {
    if (!checkedText || !containingText) {
      return false;
    }
    return checkedText.toUpperCase().indexOf(containingText.toUpperCase()) > -1;
  }

}
