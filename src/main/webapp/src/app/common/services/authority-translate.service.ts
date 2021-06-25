import {Injectable} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Authority} from '../api/api-models';

export interface AuthorityTranslateModel {
  name: string;
  description: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthorityTranslateService {

  private static readonly PREFIX_TRANSLATE_KEY: string = 'authorities.';
  private static readonly SUFFIX_TRANSLATE_KEY_NAME: string = '.name';
  private static readonly SUFFIX_TRANSLATE_KEY_DESCRIPTION: string = '.description';

  constructor(
    private translateService: TranslateService
  ) {
  }

  translateAndSort(authorities: Authority[]): AuthorityTranslateModel[] {
    if (!authorities || authorities.length < 1) {
      return [];
    }
    return authorities.map(authority => this.translate(authority))
      .sort((a, b) =>
        (a.name > b.name) ? 1 : ((b.name > a.name) ? -1 : 0)
      );
  }

  translate(authority: Authority): AuthorityTranslateModel {
    return {
      name: this.translateName(authority),
      description: this.translateDescription(authority)
    };
  }

  translateName(authority: Authority): string {
    const translatePrefix: string = AuthorityTranslateService.prepareTranslatePrefix(authority);
    return this.translateService.instant(translatePrefix + AuthorityTranslateService.SUFFIX_TRANSLATE_KEY_NAME);
  }

  translateDescription(authority: Authority): string {
    const translatePrefix: string = AuthorityTranslateService.prepareTranslatePrefix(authority);
    return this.translateService.instant(translatePrefix + AuthorityTranslateService.SUFFIX_TRANSLATE_KEY_DESCRIPTION)
  }

  private static prepareTranslatePrefix(authority: Authority): string {
    return AuthorityTranslateService.PREFIX_TRANSLATE_KEY + authority;
  }

}
