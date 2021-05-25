import {Pipe, PipeTransform} from '@angular/core';
import {AuthHelper} from '../../core/auth/auth.helper';
import {Authority} from '../api/api-models';

@Pipe({
  name: 'checkAccess'
})
export class CheckAccessPipe implements PipeTransform {

  constructor(
    private authHelper: AuthHelper
  ) {
  }

  transform(authorities: Authority[]): boolean {
    return this.authHelper.hasAuthorities(authorities);
  }

}
