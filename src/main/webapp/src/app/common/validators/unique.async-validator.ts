import {AbstractControl, AsyncValidatorFn} from '@angular/forms';
import {of} from 'rxjs';
import {debounceTime, distinctUntilChanged, map, switchMap, take, tap} from 'rxjs/operators';

export class UniqueAsyncValidator {

  static uniqueFn(service: any, methodName: string, errorKey: string, id?: number): AsyncValidatorFn {
    return ((control: AbstractControl) => {
      if (!control.valueChanges || control.pristine || !control.value) {
        return of(null);
      } else {
        return control.valueChanges.pipe(
          debounceTime(200),
          distinctUntilChanged(),
          take(1),
          switchMap(() => service[methodName](control.value, id)),
          tap(() => control.markAllAsTouched()),
          map(exists => exists ? {[errorKey]: true} : null)
        );
      }
    });
  }

}
