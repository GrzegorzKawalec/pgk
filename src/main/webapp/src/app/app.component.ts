import {registerLocaleData} from '@angular/common';

import localePL from '@angular/common/locales/pl';
import {Component} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DEFAULT_LANG} from './core/const/lang';
import {LocalStorageKey} from './core/const/local-storage-key';
import {LocalStorageService} from './core/service/local-storage.service';

@Component({
  selector: 'pgk-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(
    private translate: TranslateService,
    private localStorageService: LocalStorageService
  ) {
    this.initLang();
  }

  private initLang(): void {
    this.translate.setDefaultLang(DEFAULT_LANG);
    this.translate.use(this.getCurrentLang() || document.documentElement.lang).subscribe();
    document.documentElement.lang = this.translate.currentLang;
    registerLocaleData(localePL);
  }

  private getCurrentLang(): string {
    const lastLang: string = this.localStorageService.read(LocalStorageKey.LS_LANG);
    return lastLang ? lastLang : DEFAULT_LANG;
  }

}
