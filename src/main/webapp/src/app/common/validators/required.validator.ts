import {FormControl, ValidationErrors, ValidatorFn, Validators} from '@angular/forms';

export class RequiredValidator {

  static readonly required: ValidatorFn = RequiredValidator.requiredFn();
  static readonly requiredTrim: ValidatorFn = RequiredValidator.requiredTrimFn();

  static requiredErr(error: ValidationErrors): boolean {
    return !!error.required;
  }

  static requiredTrimErr(error: ValidationErrors): boolean {
    return !!error.requiredTrim;
  }

  private static requiredFn(): ValidatorFn {
    return Validators.required;
  }

  private static requiredTrimFn(): ValidatorFn {
    return (control: FormControl) => {
      if (control.value == null || (typeof control.value === 'string' && control.value.trim().length === 0)) {
        return {'requiredTrim': true};
      }
      return null;
    };
  }

}
