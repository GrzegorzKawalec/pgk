import {FormControl, ValidatorFn} from '@angular/forms';

export class UrlValidator {

  static readonly url: ValidatorFn = UrlValidator.urlFn();

  private static readonly URL_REGEX: RegExp = new RegExp('^(?:http(s)?:\\/\\/)?[\\w.-]+(?:\\.[\\w\\.-]+)+[\\w\\-\\._~:/?#[\\]@!\\$&\'\\(\\)\\*\\+,;=.]+$');

  private static urlFn(): ValidatorFn {
    return (control: FormControl) => {
      if (!control.value || typeof control.value !== 'string' || !UrlValidator.URL_REGEX.test(control.value)) {
        return {'url': true};
      }
      return null;
    };
  }

}
