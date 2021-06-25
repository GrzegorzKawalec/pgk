import {Component, Inject} from '@angular/core';
import {MAT_SNACK_BAR_DATA} from '@angular/material/snack-bar';
import {TranslateService} from '@ngx-translate/core';
import {ExceptionInfoModel} from './exception-info.model';

@Component({
  template: `
    <div class="info-container">
      <span class="message">{{message}}</span>
      <span *ngIf="uuid.length" class="error-uuid">{{uuid}}</span>
    </div>
  `,
  styles: [`
    .info-container {
      text-align: center;
    }

    .message {
      font-size: 1em;
      margin-bottom: 1px;
    }

    .error-uuid {
      display: block;
      margin-top: 1px;
      font-size: 0.85em;
      color: palevioletred;
    }
  `]
})
export class ExceptionInfoBodyComponent {

  private static readonly PREFIX_TRANSLATE_KEY: string = 'exception.';

  uuid: string = '';
  message: string = '';

  constructor(
    private translateService: TranslateService,
    @Inject(MAT_SNACK_BAR_DATA) public data: ExceptionInfoModel
  ) {
    this.uuid = data.uuid || '';
    this.message = this.getTranslateMessage(data).trim();
  }

  private getTranslateMessage(errorModel: ExceptionInfoModel): string {
    if (errorModel.incorrectLoginData) {
      return  this.translateService.instant(ExceptionInfoBodyComponent.PREFIX_TRANSLATE_KEY + '_incorrect_login_data');
    } else {
      let translateMessage: string = this.translateMessage(errorModel);
      if (errorModel.status === 400) {
        translateMessage = this.getPrefixForBadRequestStatus() + ' ' + translateMessage;
      }
      return translateMessage || '';
    }
  }

  private translateMessage(errorModel: ExceptionInfoModel): string {
    const translate: string = this.translateService.instant(ExceptionInfoBodyComponent.PREFIX_TRANSLATE_KEY + errorModel.type);
    return translate.startsWith(ExceptionInfoBodyComponent.PREFIX_TRANSLATE_KEY) ?
      errorModel.type : translate.trim();
  }

  private getPrefixForBadRequestStatus(): string {
    const translate: string = this.translateService.instant(ExceptionInfoBodyComponent.PREFIX_TRANSLATE_KEY + '_bad_request');
    return translate.trim();
  }

}
