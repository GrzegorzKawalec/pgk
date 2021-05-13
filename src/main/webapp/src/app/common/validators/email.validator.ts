import {ValidationErrors, ValidatorFn, Validators} from '@angular/forms';

export class EmailValidator {

  static readonly email: ValidatorFn = EmailValidator.emailFn();

  static emailErr(error: ValidationErrors): boolean {
    return !!error.email;
  }

  private static emailFn(): ValidatorFn {
    return Validators.email;
  }

}
